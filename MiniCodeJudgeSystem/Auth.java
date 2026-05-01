package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;

public class Auth {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // For handling REST API
    private static final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    private static final String API_URL = "http://localhost:3000/api";
    private static final String USERS_FILE = "users.txt";

    // State transfer between steps
    private String tempEmail = "";

    public Auth() {
        frame = new JFrame("ZenCode - Authentication");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createSignupStep1(), "SIGNUP_1");
        cardPanel.add(createSignupStep2(), "SIGNUP_2");
        cardPanel.add(createForgotPassStep1(), "FORGOT_1");
        cardPanel.add(createForgotPassStep2(), "FORGOT_2");
        
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new GridBagLayout());
        bg.add(cardPanel);
        
        frame.add(bg);
        frame.setLocationRelativeTo(null);
    }

    public void show() { frame.setVisible(true); }
    
    private JPanel createGlassCard(String title, String subtitle) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(450, 600));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel logo = new JLabel(title);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(180, 180, 180));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(logo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(sub);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        return panel;
    }
    
    private void addInput(JPanel parent, String labelText, JComponent field) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 5));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(350, 65));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(200, 200, 200));
        
        field.setPreferredSize(new Dimension(350, 40));
        field.setBackground(new Color(35, 35, 35));
        field.setOpaque(true);
        field.setForeground(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        if (field instanceof JTextField) {
            ((JTextField)field).setCaretColor(Color.WHITE);
        }

        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);
        parent.add(wrapper);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(350, 45));
        btn.setPreferredSize(new Dimension(350, 45));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    // --- SCREEN 1: LOGIN ---
    private JPanel createLoginPanel() {
        JPanel card = createGlassCard("ZenCode", "Sign in to your workspace");
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        
        addInput(card, "Email Address", emailField);
        addInput(card, "Password", passField);
        
        JCheckBox rememberMe = new JCheckBox("Remember Me");
        rememberMe.setOpaque(false);
        rememberMe.setForeground(Color.WHITE);
        rememberMe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberMe.setAlignmentX(Component.CENTER_ALIGNMENT);
        rememberMe.setFocusPainted(false);

        // Load saved credentials
        File rememberFile = new File("remember.txt");
        if (rememberFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(rememberFile))) {
                String savedEmail = br.readLine();
                String savedPass = br.readLine();
                if (savedEmail != null && savedPass != null) {
                    emailField.setText(savedEmail);
                    passField.setText(savedPass);
                    rememberMe.setSelected(true);
                }
            } catch (Exception ignored) {}
        }
        
        JButton loginBtn = createBtn("Login", new Color(44, 187, 93));
        JButton signupBtn = createBtn("Create an Account", new Color(60, 60, 60));
        
        // Custom link for forgot password
        JLabel forgotLbl = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotLbl.setForeground(new Color(138, 43, 226));
        forgotLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotLbl.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { cardLayout.show(cardPanel, "FORGOT_1"); }
        });

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword());
            if (authenticate(email, hashPassword(pass))) {
                if (rememberMe.isSelected()) {
                    try (PrintWriter pw = new PrintWriter(new FileWriter("remember.txt"))) {
                        pw.println(email);
                        pw.println(pass);
                    } catch (Exception ignored) {}
                } else {
                    new File("remember.txt").delete();
                }
                frame.dispose();
                new AppFrame(email.split("@")[0]).show();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Email or Password");
            }
        });
        
        signupBtn.addActionListener(e -> cardLayout.show(cardPanel, "SIGNUP_1"));
        
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(rememberMe);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(forgotLbl);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(signupBtn);
        return card;
    }

    // --- SCREEN 2: SIGNUP STEP 1 (EMAIL) ---
    private JPanel createSignupStep1() {
        JPanel card = createGlassCard("Create Account", "Step 1: Verify your email");
        JTextField emailField = new JTextField();
        
        addInput(card, "Email Address", emailField);
        
        JButton nextBtn = createBtn("Send Verification OTP", new Color(44, 187, 93));
        JButton backBtn = createBtn("Back to Login", new Color(60, 60, 60));
        
        nextBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an email address."); return; }
            if (!email.contains("@") || !email.contains(".")) { JOptionPane.showMessageDialog(frame, "Please enter a valid email address."); return; }
            if (isEmailRegistered(email)) {
                JOptionPane.showMessageDialog(frame, "This email is already registered. Please login.");
                return;
            }
            
            nextBtn.setEnabled(false);
            nextBtn.setText("Sending...");
            
            sendOtpApi(email, (success, message) -> {
                nextBtn.setEnabled(true);
                nextBtn.setText("Send Verification OTP");
                if (success) {
                    tempEmail = email;
                    JOptionPane.showMessageDialog(frame, "✅ OTP sent to " + email + "\nPlease check your inbox.");
                    cardLayout.show(cardPanel, "SIGNUP_2");
                } else {
                    JOptionPane.showMessageDialog(frame, "❌ " + message);
                }
            });
        });
        
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(nextBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(backBtn);
        return card;
    }

    // --- SCREEN 3: SIGNUP STEP 2 (OTP & PASS) ---
    private JPanel createSignupStep2() {
        JPanel card = createGlassCard("Secure Account", "Step 2: Create a password");
        JTextField otpField = new JTextField();
        JPasswordField p1 = new JPasswordField();
        JPasswordField p2 = new JPasswordField();
        
        addInput(card, "6-Digit OTP from Email", otpField);
        addInput(card, "New Password", p1);
        addInput(card, "Confirm Password", p2);
        
        JButton registerBtn = createBtn("Verify & Register", new Color(44, 187, 93));
        JButton backBtn = createBtn("Cancel", new Color(60, 60, 60));
        
        registerBtn.addActionListener(e -> {
            String otp = otpField.getText().trim();
            String pass1 = new String(p1.getPassword());
            String pass2 = new String(p2.getPassword());
            
            if (otp.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter the OTP from your email."); return; }
            if (otp.length() != 6) { JOptionPane.showMessageDialog(frame, "OTP must be exactly 6 digits."); return; }
            if (pass1.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter a password."); return; }
            if (pass1.length() < 6) { JOptionPane.showMessageDialog(frame, "Password must be at least 6 characters."); return; }
            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!");
                return;
            }
            
            registerBtn.setEnabled(false);
            registerBtn.setText("Verifying...");
            
            verifyOtpApi(tempEmail, otp, (success, message) -> {
                registerBtn.setEnabled(true);
                registerBtn.setText("Verify & Register");
                if (success) {
                    registerUser(tempEmail, hashPassword(pass1));
                    JOptionPane.showMessageDialog(frame, "✅ Account created! You can now login.");
                    cardLayout.show(cardPanel, "LOGIN");
                    otpField.setText(""); p1.setText(""); p2.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "❌ " + message);
                }
            });
        });
        
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "SIGNUP_1"));
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(registerBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(backBtn);
        return card;
    }

    // --- SCREEN 4: FORGOT PASSWORD STEP 1 ---
    private JPanel createForgotPassStep1() {
        JPanel card = createGlassCard("Reset Password", "Recover your account");
        JTextField emailField = new JTextField();
        
        addInput(card, "Account Email Address", emailField);
        
        JButton nextBtn = createBtn("Send Recovery OTP", new Color(138, 43, 226)); // Purple for recovery
        JButton backBtn = createBtn("Back to Login", new Color(60, 60, 60));
        
        nextBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter your email address."); return; }
            if (!isEmailRegistered(email)) {
                JOptionPane.showMessageDialog(frame, "This email is not registered. Please sign up first.");
                return;
            }
            
            nextBtn.setEnabled(false);
            nextBtn.setText("Sending...");
            
            sendOtpApi(email, (success, message) -> {
                nextBtn.setEnabled(true);
                nextBtn.setText("Send Recovery OTP");
                if (success) {
                    tempEmail = email;
                    JOptionPane.showMessageDialog(frame, "✅ Recovery OTP sent to " + email + "\nPlease check your inbox.");
                    cardLayout.show(cardPanel, "FORGOT_2");
                } else {
                    JOptionPane.showMessageDialog(frame, "❌ " + message);
                }
            });
        });
        
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(nextBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(backBtn);
        return card;
    }

    // --- SCREEN 5: FORGOT PASSWORD STEP 2 ---
    private JPanel createForgotPassStep2() {
        JPanel card = createGlassCard("New Password", "Create your new password");
        JTextField otpField = new JTextField();
        JPasswordField p1 = new JPasswordField();
        JPasswordField p2 = new JPasswordField();
        
        addInput(card, "6-Digit Recovery OTP", otpField);
        addInput(card, "New Password", p1);
        addInput(card, "Confirm Password", p2);
        
        JButton resetBtn = createBtn("Verify & Reset Password", new Color(138, 43, 226));
        JButton backBtn = createBtn("Cancel", new Color(60, 60, 60));
        
        resetBtn.addActionListener(e -> {
            String otp = otpField.getText().trim();
            String pass1 = new String(p1.getPassword());
            String pass2 = new String(p2.getPassword());
            
            if (otp.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter the OTP from your email."); return; }
            if (otp.length() != 6) { JOptionPane.showMessageDialog(frame, "OTP must be exactly 6 digits."); return; }
            if (pass1.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter a new password."); return; }
            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!");
                return;
            }
            
            resetBtn.setEnabled(false);
            resetBtn.setText("Verifying...");
            
            verifyOtpApi(tempEmail, otp, (success, message) -> {
                resetBtn.setEnabled(true);
                resetBtn.setText("Verify & Reset Password");
                if (success) {
                    updatePassword(tempEmail, hashPassword(pass1));
                    JOptionPane.showMessageDialog(frame, "✅ Password reset successful! You can now login.");
                    cardLayout.show(cardPanel, "LOGIN");
                    otpField.setText(""); p1.setText(""); p2.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "❌ " + message);
                }
            });
        });
        
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "FORGOT_1"));
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(resetBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(backBtn);
        return card;
    }

    // --- LOCAL FILE LOGIC ---
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex) { return ""; }
    }

    private boolean isEmailRegistered(String email) {
        File f = new File(USERS_FILE);
        if (!f.exists()) return false;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = r.readLine()) != null) {
                String[] p = l.split(":", -1);
                if (p.length >= 1 && p[0].equals(email)) return true;
            }
        } catch (IOException e) {}
        return false;
    }

    private boolean authenticate(String email, String hashedPass) {
        File f = new File(USERS_FILE);
        if (!f.exists()) return false;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = r.readLine()) != null) {
                String[] p = l.split(":", -1);
                if (p.length >= 2 && p[0].equals(email) && p[1].equals(hashedPass)) return true;
            }
        } catch (IOException e) {}
        return false;
    }

    private void registerUser(String email, String hashedPass) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            w.write(email + ":" + hashedPass + "::0");
            w.newLine();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void updatePassword(String email, String newHashedPass) {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = r.readLine()) != null) {
                String[] p = l.split(":", -1);
                if (p.length >= 2 && p[0].equals(email)) {
                    // reconstructed with new password
                    sb.append(p[0]).append(":").append(newHashedPass);
                    for (int i = 2; i < p.length; i++) {
                        sb.append(":").append(p[i]);
                    }
                    sb.append(System.lineSeparator());
                } else {
                    sb.append(l).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {}
        
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            w.write(sb.toString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- NODE.JS API CALLS ---
    // Callback interface now returns both success flag AND the server message/error
    private interface ApiCallback { void onResult(boolean success, String message); }

    /** Extracts a JSON string field value from a raw JSON response body */
    private String extractJsonString(String json, String key) {
        try {
            String search = "\"" + key + "\":\"";
            int start = json.indexOf(search);
            if (start == -1) return "";
            start += search.length();
            int end = json.indexOf("\"", start);
            return end == -1 ? "" : json.substring(start, end);
        } catch (Exception e) { return ""; }
    }

    private void sendOtpApi(String email, ApiCallback callback) {
        SwingWorker<String[], Void> worker = new SwingWorker<>() {
            @Override
            protected String[] doInBackground() {
                try {
                    String json = "{\"email\":\"" + email + "\"}";
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + "/send-otp"))
                        .header("Content-Type", "application/json")
                        .version(java.net.http.HttpClient.Version.HTTP_1_1)
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
                    HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
                    boolean ok = res.statusCode() == 200 && res.body().contains("\"success\":true");
                    String msg = ok
                        ? extractJsonString(res.body(), "message")
                        : extractJsonString(res.body(), "error");
                    return new String[]{ String.valueOf(ok), msg };
                } catch (Exception e) {
                    return new String[]{ "false", "Cannot reach server. Make sure the Node.js backend is running." };
                }
            }
            @Override protected void done() {
                try {
                    String[] r = get();
                    callback.onResult("true".equals(r[0]), r[1]);
                } catch (Exception e) {
                    callback.onResult(false, "An unexpected error occurred.");
                }
            }
        };
        worker.execute();
    }

    private void verifyOtpApi(String email, String otp, ApiCallback callback) {
        SwingWorker<String[], Void> worker = new SwingWorker<>() {
            @Override
            protected String[] doInBackground() {
                try {
                    String json = "{\"email\":\"" + email + "\",\"otp\":\"" + otp + "\"}";
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + "/verify-otp"))
                        .header("Content-Type", "application/json")
                        .version(java.net.http.HttpClient.Version.HTTP_1_1)
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
                    HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
                    boolean ok = res.statusCode() == 200 && res.body().contains("\"success\":true");
                    String msg = ok
                        ? extractJsonString(res.body(), "message")
                        : extractJsonString(res.body(), "error");
                    return new String[]{ String.valueOf(ok), msg };
                } catch (Exception e) {
                    return new String[]{ "false", "Cannot reach server. Make sure the Node.js backend is running." };
                }
            }
            @Override protected void done() {
                try {
                    String[] r = get();
                    callback.onResult("true".equals(r[0]), r[1]);
                } catch (Exception e) {
                    callback.onResult(false, "An unexpected error occurred.");
                }
            }
        };
        worker.execute();
    }
}
