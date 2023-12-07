@echo off
if "%1"=="" (
    echo Veuillez fournir un nom ou chemin de script en argument.
    echo Par exemple "Switch" pour executer le script "Switch.groovy" présent dans le dossier "scripts" ou bien scripts/Switch.groovy
    exit /b 1
)

for %%I in ("%1") do set SCRIPT_NAME=%%~nI

set OUTPUT_FILE=%SCRIPT_NAME%.ino

java -jar target\dsl-groovy-1.0-jar-with-dependencies.jar scripts\%SCRIPT_NAME%.groovy > results\%OUTPUT_FILE%
