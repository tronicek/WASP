WASP_JAR="/home/TRONZ635/research/WASP/WASP-1.0.jar"
OPTIONS="-Xmx8G"

java $OPTIONS -cp $WASP_JAR english.Evaluation linux.properties 8 12 > times.txt
