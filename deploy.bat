@echo off
set "JDK_BIN=C:\Program Files\Java\jdk-25.0.2\bin"

echo Cleaning old files...
del MiniCodeJudgeSystem\*.class /s /q
del ZenCode.jar /q

echo Compiling ZenCode...
"%JDK_BIN%\javac.exe" MiniCodeJudgeSystem/*.java

echo Packaging ZenCode.jar...
"%JDK_BIN%\jar.exe" cfm ZenCode.jar manifest.txt MiniCodeJudgeSystem/*.class MiniCodeJudgeSystem/icons/*.png

echo Deployment complete! Use 'java -jar ZenCode.jar' to run.
pause
