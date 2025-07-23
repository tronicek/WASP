@echo off
set WASP_JAR="/research/projects/WASP/target/WASP-1.0.jar"
set OPTIONS=-Xmx8G

java %OPTIONS% -cp %WASP_JAR% english.Evaluation win.properties 8 12 > times.txt
