@echo off
setlocal

:: Set the project root directory (where pom.xml is located)
set PROJECT_ROOT=%~dp0

:: Set the source and target directories
set SRC_DIR=%PROJECT_ROOT%src\main\java
set OUTPUT_DIR=%PROJECT_ROOT%target\classes

:: Create output directory if it doesn't exist
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

:: Compile all Java files in the source directory
javac -d "%OUTPUT_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\dev\nara\Main.java" "%SRC_DIR%\dev\nara\CoinFlipDistribution.java" "%SRC_DIR%\dev\nara\RandomVariableCalculator.java"

:: Check if compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed
    pause
    exit /b %errorlevel%
)

:: Run the Main class
java -cp "%OUTPUT_DIR%" dev.nara.Main

:: Pause to see the output
pause