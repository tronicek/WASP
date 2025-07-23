@echo off
set WASP_JAR="/research/projects/WASP/target/WASP-1.0.jar"
set OPTIONS=-Xmx8G

java %OPTIONS% -cp %WASP_JAR% binarylongpattern.Evaluation win.properties 50 54 > times.txt
java %OPTIONS% -cp %WASP_JAR% binarylongpattern.Evaluation2 win2.properties 50 54 > times2.txt
