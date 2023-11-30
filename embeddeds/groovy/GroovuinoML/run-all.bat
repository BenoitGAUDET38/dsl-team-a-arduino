@echo off

for %%i in (scripts\*.groovy) do (
    call run.bat %%~ni
)

echo Tous les scripts ont ete executes.
