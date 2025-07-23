WASP_JAR="/home/TRONZ635/research/WASP/WASP-1.0.jar"
OPTIONS="-Xmx8G"

java $OPTIONS -cp $WASP_JAR binary.Evaluation linux.properties 8 12 > times.txt
java $OPTIONS -cp $WASP_JAR binary.Evaluation2 linux2.properties 8 12 > times2.txt
java $OPTIONS -cp $WASP_JAR binary.Evaluation3 linux.properties 8 12 > times3.txt
