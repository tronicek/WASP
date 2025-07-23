WASP_JAR="/home/TRONZ635/research/WASP/WASP-1.0.jar"
OPTIONS="-Xmx8G"

java $OPTIONS -cp $WASP_JAR binarylongpattern.Evaluation linux.properties 50 54 > times.txt
java $OPTIONS -cp $WASP_JAR binarylongpattern.Evaluation2 linux2.properties 50 54 > times2.txt
