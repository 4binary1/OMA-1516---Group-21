@echo off 
del /Q output\solutions.csv
set MYPROG=java -jar solverVRP.jar

for /F %%i in (files.txt) do (
	echo %MYPROG% -i %%i
	%MYPROG% -i %%i
)


