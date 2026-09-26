#!/bin/bash
#nettoyage au prealable
killall orbd 2>/dev/null
# 1. Compilation des sources Java
/usr/lib/jvm/java-8-openjdk-amd64/bin/javac -d bin $(find src -name "*.java")

# 2. Lancement du service de noms en arrière-plan (&) + temps de démarrage
orbd -ORBInitialPort 1050 &
sleep 2

# 3. Lancement du Serveur en arrière-plan (&) + temps d'initialisation
/usr/lib/jvm/java-8-openjdk-amd64/bin/java -cp bin Serveur -ORBInitialPort 1050 -ORBInitialHost localhost &
sleep 2

# 4. Lancement du Client (avec -cp bin pour charger les classes)
/usr/lib/jvm/java-8-openjdk-amd64/bin/java -cp bin Client -ORBInitialHost localhost -ORBInitialPort 1050