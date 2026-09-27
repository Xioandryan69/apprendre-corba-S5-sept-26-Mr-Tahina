#!/bin/bash
# 1. Génération des stubs/skeletons IDL 
set -e
cd src
export OMNIORB_CONFIG="/home/huhu/S5/Mr Tahina/CORBA/24 sept/apprendre-corba-S5-sept-26-Mr-Tahina/omniORB.cfg"
# 2. Compilation des fichiers objets (.o) 
g++ -c CalculateurSK.cc -o CalculateurSK.o
g++ -c Serveur.cpp -o Serveur.o
g++ -c Client.cpp -o Client.o

# 3. Édition de liens pour le Serveur et le Client 
g++ CalculateurSK.o Serveur.o -lomniORB4 -lomnithread -lCOS4 -o serveur
g++ CalculateurSK.o Client.o -lomniORB4 -lomnithread -lCOS4 -o client

echo "Compilation terminée avec succès ! "
# ./serveur -ORBInitRef NameService=corbaloc::localhost:2809/NameService
# ./client -ORBInitRef NameService=corbaloc::localhost:2809/NameService


#serveur c++ client java
# /usr/lib/jvm/java-8-openjdk-amd64/bin/java -cp bin Client -ORBInitRef NameService=corbaloc::localhost:2809/NameService

