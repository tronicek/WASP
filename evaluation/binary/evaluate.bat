@echo off
set WASP_JAR="/research/projects/WASP/target/WASP-1.0.jar"
set OPTIONS=-Xmx8G

java %OPTIONS% -cp %WASP_JAR% binary.Evaluation win.properties 8 12 > times.txt
java %OPTIONS% -cp %WASP_JAR% binary.Evaluation2 win2.properties 8 12 > times2.txt
java %OPTIONS% -cp %WASP_JAR% binary.Evaluation3 win.properties 8 12 > times3.txt
