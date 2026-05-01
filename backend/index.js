require('dotenv').config();

const express = require('express');
const cors = require('cors');
const WebSocket = require('ws');
const http = require('http');
const rateLimit = require('express-rate-limit');
const crypto = require('crypto');

const app = express();
app.use(cors());
app.use(express.json());

const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

// ─── OTP Store ──────────────────────────────────────────────────────────────
// Structure: email -> { otp, expiresAt, attempts }
const otpStore = new Map();

const OTP_EXPIRY_MS = 5 * 60 * 1000; // 5 minutes
const MAX_ATTEMPTS  = 3;              // max wrong guesses before lockout

// ─── Rate Limiter: max 3 send-otp requests per 10 minutes per IP ─────────────
const otpSendLimiter = rateLimit({
    windowMs: 10 * 60 * 1000,
    max: 3,
    message: { error: 'Too many OTP requests. Please wait 10 minutes before trying again.' },
    standardHeaders: true,
    legacyHeaders: false,
});

// ─── Email Validation ─────────────────────────────────────────────────────────
function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// ─── Brevo HTTP Email Sender (bypasses SMTP port blocks) ────────────────────
const isEmailConfigured = process.env.BREVO_API_KEY &&
    !process.env.BREVO_API_KEY.includes('paste_your');

async function sendOTPEmail(toEmail, otp) {
    const response = await fetch('https://api.brevo.com/v3/smtp/email', {
        method: 'POST',
        headers: {
            'accept': 'application/json',
            'api-key': process.env.BREVO_API_KEY,
            'content-type': 'application/json',
        },
        body: JSON.stringify({
            sender: {
                name: 'ZenCode',
                email: process.env.EMAIL_USER,
            },
            to: [{ email: toEmail }],
            subject: 'Your ZenCode OTP \u2014 expires in 5 minutes',
            htmlContent: `
            <div style="font-family: 'Segoe UI', Arial, sans-serif; max-width: 480px; margin: 0 auto; background: #0f0f0f; color: #e0e0e0; border-radius: 12px; overflow: hidden;">
                <div style="background: linear-gradient(135deg, #1a1a2e, #16213e); padding: 32px; text-align: center;">
                    <h1 style="color: #2cbb5d; margin: 0; font-size: 28px; letter-spacing: 2px;">ZenCode</h1>
                    <p style="color: #aaa; margin: 8px 0 0; font-size: 14px;">Secure Authentication</p>
                </div>
                <div style="padding: 32px; text-align: center;">
                    <p style="color: #ccc; font-size: 16px; margin: 0 0 24px;">Your One-Time Password is:</p>
                    <div style="background: #1a1a1a; border: 2px solid #2cbb5d; border-radius: 12px; padding: 20px; display: inline-block; margin: 0 auto;">
                        <span style="font-size: 42px; font-weight: bold; letter-spacing: 12px; color: #2cbb5d; font-family: 'Courier New', monospace;">${otp}</span>
                    </div>
                    <p style="color: #ff6b6b; font-size: 13px; margin: 20px 0 0;">&#9201; This OTP expires in <strong>5 minutes</strong>.</p>
                    <p style="color: #888; font-size: 12px; margin: 8px 0 0;">Do not share this code with anyone. ZenCode will never ask for your OTP.</p>
                </div>
                <div style="background: #1a1a1a; padding: 16px; text-align: center;">
                    <p style="color: #555; font-size: 11px; margin: 0;">If you did not request this OTP, you can safely ignore this email.</p>
                </div>
            </div>`,
        }),
    });

    if (!response.ok) {
        const errBody = await response.json().catch(() => ({}));
        throw new Error(errBody.message || `Brevo API error: ${response.status}`);
    }
    return response.json();
}

// ─── POST /api/send-otp ───────────────────────────────────────────────────────
app.post('/api/send-otp', otpSendLimiter, async (req, res) => {
    const { email } = req.body;

    if (!email || !isValidEmail(email)) {
        return res.status(400).json({ error: 'Please provide a valid email address.' });
    }

    // Generate a cryptographically secure 6-digit OTP
    const otp = (crypto.randomInt(100000, 999999)).toString();
    const expiresAt = Date.now() + OTP_EXPIRY_MS;

    // Store OTP securely on server only
    otpStore.set(email.toLowerCase(), { otp, expiresAt, attempts: 0 });

    // Auto-delete from store after expiry
    setTimeout(() => otpStore.delete(email.toLowerCase()), OTP_EXPIRY_MS + 1000);

    if (!isEmailConfigured) {
        // Developer mode fallback — print to console only
        console.log('\n==================== DEV MODE ====================');
        console.log(`  OTP for ${email}: ${otp}  (expires in 5 min)`);
        console.log('===================================================\n');
        return res.json({ success: true, message: 'OTP logged to server console (Dev Mode — set EMAIL_USER and EMAIL_PASS in .env to send real emails).' });
    }

    // Send real email via Brevo HTTP API
    try {
        await sendOTPEmail(email, otp);
        console.log(`[OTP SENT] → ${email}`);
        return res.json({ success: true, message: 'OTP sent to your email. Please check your inbox.' });
    } catch (err) {
        console.error('[EMAIL ERROR]', err.message);
        return res.status(500).json({ error: 'Failed to send OTP. Please check your email address and try again.' });
    }
});

// ─── POST /api/verify-otp ─────────────────────────────────────────────────────
app.post('/api/verify-otp', (req, res) => {
    const { email, otp } = req.body;

    if (!email || !otp) {
        return res.status(400).json({ error: 'Email and OTP are required.' });
    }

    const record = otpStore.get(email.toLowerCase());

    if (!record) {
        return res.status(400).json({ error: 'No OTP was sent to this email, or it has already expired. Please request a new one.' });
    }

    // Check expiry
    if (Date.now() > record.expiresAt) {
        otpStore.delete(email.toLowerCase());
        return res.status(400).json({ error: 'OTP has expired. Please request a new one.' });
    }

    // Check attempt limit
    if (record.attempts >= MAX_ATTEMPTS) {
        otpStore.delete(email.toLowerCase());
        return res.status(400).json({ error: 'Too many incorrect attempts. Please request a new OTP.' });
    }

    // Verify OTP — use timing-safe comparison
    const inputHash   = crypto.createHash('sha256').update(otp).digest('hex');
    const storedHash  = crypto.createHash('sha256').update(record.otp).digest('hex');
    const isMatch     = crypto.timingSafeEqual(Buffer.from(inputHash), Buffer.from(storedHash));

    if (isMatch) {
        otpStore.delete(email.toLowerCase());
        console.log(`[OTP VERIFIED] ✓ ${email}`);
        return res.json({ success: true, message: 'OTP verified successfully.' });
    } else {
        record.attempts++;
        const remaining = MAX_ATTEMPTS - record.attempts;
        console.log(`[OTP FAILED] ${email} — ${record.attempts} wrong attempt(s)`);
        return res.status(400).json({
            error: remaining > 0
                ? `Incorrect OTP. You have ${remaining} attempt(s) remaining.`
                : 'Too many incorrect attempts. Please request a new OTP.'
        });
    }
});

// ─── WebSocket Server for Collaborative Coding ────────────────────────────────
const rooms = new Map();

wss.on('connection', (ws) => {
    ws.on('message', (messageAsString) => {
        try {
            const message = JSON.parse(messageAsString);

            if (message.type === 'join') {
                const roomId = message.roomId;
                ws.roomId = roomId;
                if (!rooms.has(roomId)) rooms.set(roomId, new Set());
                rooms.get(roomId).add(ws);
                console.log(`[WS] Client joined room: ${roomId}`);
                ws.send(JSON.stringify({ type: 'system', content: `Joined room ${roomId}` }));
            } else if (message.type === 'code_update') {
                const roomId = message.roomId;
                if (rooms.has(roomId)) {
                    for (const client of rooms.get(roomId)) {
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({ type: 'code_update', code: message.code }));
                        }
                    }
                }
            } else if (message.type === 'battle_update' || message.type === 'battle_win') {
                const roomId = message.roomId;
                if (rooms.has(roomId)) {
                    for (const client of rooms.get(roomId)) {
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify(message));
                        }
                    }
                }
            }
        } catch (err) {
            console.error('[WS ERROR] Invalid message format', err.message);
        }
    });

    ws.on('close', () => {
        if (ws.roomId && rooms.has(ws.roomId)) {
            rooms.get(ws.roomId).delete(ws);
            if (rooms.get(ws.roomId).size === 0) rooms.delete(ws.roomId);
            console.log(`[WS] Client left room: ${ws.roomId}`);
        }
    });
});

// ─── Start Server ─────────────────────────────────────────────────────────────
const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
    console.log(`\n🚀 ZenCode Server running on http://localhost:${PORT}`);
    console.log(`🔌 WebSocket server running on ws://localhost:${PORT}`);
    if (!isEmailConfigured) {
        console.log('\n⚠️  DEV MODE: Email not configured.');
        console.log('   → Open backend/.env and set EMAIL_USER and EMAIL_PASS with your Gmail + App Password.');
        console.log('   → OTPs will be printed here until you do.\n');
    } else {
        console.log(`📧 Email configured: ${process.env.EMAIL_USER}\n`);
    }
});
