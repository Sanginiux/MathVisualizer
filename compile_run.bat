@echo off
cd /d "%~dp0"
if not exist bin mkdir bin
echo Compiling...
javac -encoding UTF-8 -cp "lib\*" -d bin src\*.java
if %ERRORLEVEL% NEQ 0 ( echo Compilation FAILED. & pause & exit /b 1 )
echo Compilation successful! Starting app...
java -cp "lib\*;bin" Main
pause
