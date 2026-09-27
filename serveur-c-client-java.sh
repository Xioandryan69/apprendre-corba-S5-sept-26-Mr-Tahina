./execute-c.sh
cd src
./serveur -ORBInitRef NameService=corbaloc::localhost:2809/NameService

#another terminal 
/usr/lib/jvm/java-8-openjdk-amd64/bin/java -cp bin Client -ORBInitRef NameService=corbaloc::localhost:2809/NameService