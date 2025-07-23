@echo off
set WASP_JAR="/research/projects/WASP/target/WASP-1.0.jar"
set OPTIONS=-Xmx8G

java %OPTIONS% -cp %WASP_JAR% acgt.Evaluation win.properties 4 8 > times.txt
java %OPTIONS% -cp %WASP_JAR% acgt.Evaluation2 win2.properties 4 8 > times2.txt
java %OPTIONS% -cp %WASP_JAR% acgt.Evaluation3 win.properties 4 8 > times3.txt
