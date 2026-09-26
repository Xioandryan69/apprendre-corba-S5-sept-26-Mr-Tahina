#!/bin/bash

# 0. Nettoyage préalable des anciens processus
killall orbd 2>/dev/null
killall java 2>/dev/null

JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64/bin"

# 1. Compilation des sources Java
$JAVA_HOME/javac -d bin $(find src -name "*.java")

# 2. Lancement du service de noms (orbd) en arrière-plan
$JAVA_HOME/orbd -ORBInitialPort 1050 &
PID_ORBD=$!
sleep 2

# 3. Lancement du Serveur en arrière-plan
$JAVA_HOME/java -cp bin Serveur -ORBInitialPort 1050 -ORBInitialHost localhost &
PID_SERVEUR=$!
sleep 2

# 4. Lancement du Client (en premier plan)
$JAVA_HOME/java -cp bin Client -ORBInitialHost localhost -ORBInitialPort 1050

# 5. Nettoyage propre à la fin de l'exécution
echo "Arrêt des services..."
kill $PID_SERVEUR 2>/dev/null
kill $PID_ORBD 2>/dev/null