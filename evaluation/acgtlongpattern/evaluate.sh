WASP_JAR="/home/TRONZ635/research/WASP/WASP-1.0.jar"
DATA_DIR="/home/TRONZ635/research/WASP/data"
OPTIONS="-Xmx8G"

java $OPTIONS -cp $WASP_JAR acgtlongpattern.Evaluation linux.properties 50 54 > times.txt
#java $OPTIONS -cp $WASP_JAR acgt.Evaluation2 linux2.properties 4 8 > times2.txt
#java $OPTIONS -cp $WASP_JAR acgt.Evaluation3 linux.properties 4 8 > times3.txt
