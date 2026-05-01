# ⚡ ZenCode - Premium Competitive Programming Platform

ZenCode is a state-of-the-art competitive programming environment designed for high-performance interview preparation and algorithmic mastery. Built with a sleek dark-mode Java Swing UI and a robust Node.js backend.

---

## 🌟 Key Features

### 🖥️ Advanced Workspace
- **Real-time Code Execution:** Run and submit Java code locally with instant console feedback.
- **Syntax Highlighting:** Integrated high-quality syntax coloring for Java keywords and structures.
- **Hint System:** Stuck on a problem? Use **50 XP** to reveal a curated hint.
- **Custom Test Cases:** Test your logic against your own inputs before submitting.

### 🏆 Gamified Experience
- **Global Leaderboard:** Compete with top coders worldwide. Rank is based on XP, solved problems, and streaks.
- **Daily Challenges:** Earn bonus XP by solving the "Problem of the Day" featured on your dashboard.
- **XP & Levels:** Level up as you solve more complex problems (Easy, Medium, Hard).
- **Day Streaks:** Maintain your daily momentum to climb the ranks.

### 🧪 Mock Test Center
- **Lockdown Mode:** Simulate a real interview environment with time tracking and restricted navigation.
- **History Tracking:** Review your past test performance and scores.

### 🤝 Social & Collaboration
- **Live Collab Rooms:** Join rooms via WebSocket to code together with friends in real-time.
- **Battle Mode:** Challenge others to head-to-head coding battles.

---

## 🎨 Premium UI/UX
- **Dark Mode Aesthetic:** Designed for long coding sessions with minimal eye strain.
- **Standardized Color Scheme:** 
  - 🟢 **Success (Submit):** Action-oriented green.
  - 🟡 **Running (Run/Next):** Clear visibility for execution.
  - 🔴 **Danger (Reset/Start):** High-impact red for critical actions.
  - 🟣 **Special (Category/Collab):** Elegant purple for navigation.

---

## ⚙️ Setup & Installation

### Prerequisites
- **Java JDK 17+**
- **Node.js 18+** (for the backend)

### 1. Start the Backend
```bash
cd backend
npm install
npm start
```

### 2. Run the Application
You can run the compiled JAR directly:
```bash
java -jar ZenCode.jar
```
*Or use the provided script:*
```bash
.\run.bat
```

---

## 🛠️ Built With
- **Frontend:** Java Swing (Metal Look & Feel)
- **Editor:** Custom JTextPane with Regex-based Syntax Highlighting
- **Networking:** Java `HttpClient` (REST) & `WebSocket`
- **Backend:** Node.js, Express, WebSocket (`ws`)
- **Persistence:** Local file-based storage for speed and simplicity

---

## 🔒 Security
- **OTP Verification:** All logins and signups are protected via Email OTP.
- **Password Hashing:** Passwords are never stored in plain text (SHA-256 encryption).

---

### 👨‍💻 Developer Note
ZenCode is designed for the modern developer who wants a focused, high-performance environment to master Data Structures and Algorithms.

**Happy Coding!** &lt;/&gt;
