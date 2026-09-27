/usr/lib/jvm/java-8-openjdk-amd64/bin/java -cp bin Serveur -ORBInitRef NameService=corbaloc::localhost:2809/NameService

# another terminal 
./client -ORBInitRef NameService=corbaloc::localhost:2809/NameService