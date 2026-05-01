// ═══════════════ STATE ═══════════════
let currentUser = null;
let currentEditor = null;
let currentProblem = null;
let timerInterval = null;
let timerSeconds = 0;
let solvedIds = [];
let testActive = false;

// ═══════════════ INITIALIZATION ═══════════════
window.onload = () => {
  const saved = localStorage.getItem('zen_user');
  if (saved) {
    currentUser = JSON.parse(saved);
    renderApp();
  }
};

function renderApp() {
  document.getElementById('auth-screen').classList.add('hidden');
  document.getElementById('app-screen').classList.remove('hidden');
  document.getElementById('nav-username').innerText = currentUser.username;
  
  initMonaco();
  renderProblemList('All');
  updateDailyBanner();
  updateProfile();
}

// ═══════════════ AUTH ═══════════════
function showSignup() {
  document.getElementById('login-form').classList.add('hidden');
  document.getElementById('signup-form').classList.remove('hidden');
  document.getElementById('auth-subtitle').innerText = "Join the elite coding circle";
}

function showLogin() {
  document.getElementById('signup-form').classList.add('hidden');
  document.getElementById('forgot-form').classList.add('hidden');
  document.getElementById('login-form').classList.remove('hidden');
  document.getElementById('auth-subtitle').innerText = "Sign in to your workspace";
}

function showForgot() {
  document.getElementById('login-form').classList.add('hidden');
  document.getElementById('forgot-form').classList.remove('hidden');
  document.getElementById('auth-subtitle').innerText = "Reset your access";
}

function doLogin() {
  const email = document.getElementById('login-email').value;
  if (!email) return showToast("Please enter email");
  
  currentUser = {
    username: email.split('@')[0],
    email: email,
    xp: 250,
    solved: 0,
    streak: 1,
    battles: "0W - 0L",
    history: []
  };
  localStorage.setItem('zen_user', JSON.stringify(currentUser));
  renderApp();
}

function logout() {
  localStorage.removeItem('zen_user');
  location.reload();
}

// ═══════════════ UI HELPERS ═══════════════
function showTab(tabId) {
  if (testActive) return showToast("Finish the test first!", "red");
  
  document.querySelectorAll('.tab-content').forEach(el => el.classList.add('hidden'));
  document.querySelectorAll('.nav-btn').forEach(el => el.classList.remove('active'));
  
  document.getElementById(`panel-${tabId}`).classList.remove('hidden');
  document.getElementById(`tab-${tabId}`).classList.add('active');
  
  if (tabId === 'leaderboard') renderLeaderboard();
}

function showToast(msg, color = "purple") {
  const t = document.getElementById('toast');
  t.innerText = msg;
  t.style.background = color === "red" ? "var(--accent-red)" : "var(--accent-purple)";
  t.classList.add('active');
  setTimeout(() => t.classList.remove('active'), 3000);
}

// ═══════════════ PROBLEMS ═══════════════
function renderProblemList(cat) {
  const list = document.getElementById('problem-list');
  list.innerHTML = '';
  
  const filtered = cat === 'All' ? PROBLEMS : PROBLEMS.filter(p => p.category === cat);
  
  filtered.forEach(p => {
    const card = document.createElement('div');
    card.className = 'problem-card glass-card';
    card.innerHTML = `
      <div class="prob-info">
        <div class="prob-name">${p.title}</div>
        <div class="prob-diff diff-${p.difficulty.toLowerCase()}">${p.difficulty}</div>
        <div style="color:var(--text-dim);font-size:0.8rem">${p.category}</div>
      </div>
      <button class="btn btn-purple btn-sm" onclick="openProblem(${p.id})">Solve</button>
    `;
    list.appendChild(card);
  });
}

function filterProblems(cat, btn) {
  document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  renderProblemList(cat);
}

function updateDailyBanner() {
  const daily = PROBLEMS[0];
  document.getElementById('daily-title-text').innerText = daily.title;
}

function openDaily() {
  openProblem(1);
}

function openProblem(id) {
  currentProblem = PROBLEMS.find(p => p.id === id);
  showTab('workspace');
  
  document.getElementById('ws-title').innerText = currentProblem.title;
  document.getElementById('ws-desc').innerText = currentProblem.description;
  
  if (currentEditor) {
    currentEditor.setValue(currentProblem.starterCode);
  }
}

// ═══════════════ WORKSPACE ═══════════════
function initMonaco() {
  require.config({ paths: { vs: 'https://cdn.jsdelivr.net/npm/monaco-editor@0.44.0/min/vs' } });
  require(['vs/editor/editor.main'], function () {
    currentEditor = monaco.editor.create(document.getElementById('monaco-editor'), {
      value: "public class Solution {\n    public static void main(String[] args) {\n        System.out.println(\"Hello ZenCode!\");\n    }\n}",
      language: 'java',
      theme: 'vs-dark',
      automaticLayout: true,
      fontSize: 14,
      fontFamily: 'Fira Code',
      minimap: { enabled: false },
      padding: { top: 16 }
    });
  });
}

async function runCode(isSubmit) {
  const outputEl = document.getElementById('console-output');
  outputEl.innerText = "Running code...";
  outputEl.style.color = "#ddd";
  
  const code = currentEditor.getValue();
  const lang = document.getElementById('lang-select').value;
  const customInput = document.getElementById('custom-input').value;

  // Use Piston API for real execution
  try {
    const resp = await fetch('https://emkc.org/api/v2/piston/execute', {
      method: 'POST',
      body: JSON.stringify({
        language: lang,
        version: "*",
        files: [{ content: code }]
      })
    });
    const data = await resp.json();
    
    if (data.run.stderr) {
      outputEl.innerText = data.run.stderr;
      outputEl.style.color = "#f66";
    } else {
      outputEl.innerText = data.run.stdout;
      outputEl.style.color = "#2cbb5d";
      
      if (isSubmit) {
        showToast("✓ Problem Solved! +50 XP", "green");
        currentUser.xp += 50;
        currentUser.solved++;
        localStorage.setItem('zen_user', JSON.stringify(currentUser));
        updateProfile();
      }
    }
  } catch (err) {
    outputEl.innerText = "Error: API Connection Failed";
  }
}

function resetCode() {
  if (currentProblem) currentEditor.setValue(currentProblem.starterCode);
}

function showHint() {
  if (!currentProblem) return showToast("Open a problem first!");
  if (currentUser.xp < 50) return showToast("Not enough XP! (Need 50)", "red");
  
  currentUser.xp -= 50;
  showToast("Hint revealed! -50 XP", "yellow");
  document.getElementById('ws-desc').innerHTML += `<br><br><b style="color:var(--accent-orange)">💡 HINT:</b> ${currentProblem.hint}`;
  updateProfile();
  localStorage.setItem('zen_user', JSON.stringify(currentUser));
}

function startTimer() {
  if (timerInterval) clearInterval(timerInterval);
  timerSeconds = 0;
  timerInterval = setInterval(() => {
    timerSeconds++;
    const m = Math.floor(timerSeconds / 60);
    const s = timerSeconds % 60;
    document.getElementById('timer-display').innerText = `${m}:${s < 10 ? '0'+s : s}`;
  }, 1000);
}

// ═══════════════ TEST CENTER ═══════════════
function startTest() {
  testActive = true;
  document.getElementById('test-setup').classList.add('hidden');
  document.getElementById('test-active').classList.remove('hidden');
  showToast("Lockdown Started!", "red");
  // In a real browser, we can't fully "lock", but we simulate the UI
}

// ═══════════════ PROFILE / LEADERBOARD ═══════════════
function updateProfile() {
  document.getElementById('profile-name').innerText = currentUser.username;
  document.getElementById('profile-avatar').innerText = currentUser.username[0].toUpperCase();
  document.getElementById('profile-level').innerText = `Level ${Math.floor(currentUser.xp / 1000) + 1}`;
  
  const xpInLevel = currentUser.xp % 1000;
  document.getElementById('xp-bar-fill').style.width = `${(xpInLevel / 1000) * 100}%`;
  document.getElementById('xp-label').innerText = `${xpInLevel} / 1000 XP`;
  
  document.getElementById('profile-streak').innerText = currentUser.streak;
  document.getElementById('profile-battles').innerText = currentUser.battles;
  
  // Diff counts
  document.getElementById('easy-count').innerText = `${currentUser.solved}/1009`;
  document.getElementById('easy-bar').style.width = `${Math.min(100, (currentUser.solved / 100) * 100)}%`;
}

function renderLeaderboard() {
  const body = document.getElementById('lb-body');
  body.innerHTML = '';
  const users = [
    { rank: 1, name: "MasterCoder", xp: 12500, solved: 450, streak: 120 },
    { rank: 2, name: "AlgoKing", xp: 11200, solved: 390, streak: 85 },
    { rank: 3, name: currentUser.username, xp: currentUser.xp, solved: currentUser.solved, streak: currentUser.streak },
    { rank: 4, name: "BinaryBeast", xp: 8900, solved: 310, streak: 42 }
  ].sort((a,b) => b.xp - a.xp);

  users.forEach((u, i) => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${i+1}</td>
      <td style="font-weight:600">${u.name}</td>
      <td style="color:var(--accent-green)">${u.xp}</td>
      <td>${u.solved}</td>
      <td style="color:var(--accent-yellow)">${u.streak} 🔥</td>
    `;
    body.appendChild(row);
  });
}
