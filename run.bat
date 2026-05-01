@echo off
echo Starting Node.js Backend Server in a new window...
start "Node Backend" cmd /c "cd backend && npm start"

echo Cleaning old classes...
del MiniCodeJudgeSystem\*.class 2>nul
echo Compiling ZenCode...
javac MiniCodeJudgeSystem\*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Launching ZenCode...
java MiniCodeJudgeSystem.Main
pause
