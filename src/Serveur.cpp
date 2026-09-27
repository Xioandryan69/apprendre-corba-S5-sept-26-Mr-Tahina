#include <iostream>
#include <omniORB4/CORBA.h>
#include "Calculateur.hh"
#include "CalculateurImpl.cpp" // Inclusion de l'implémentation du Servant

int main(int argc, char* argv[]) {
    try {
        // 1. Initialisation de l'ORB 
        CORBA::ORB_var orb = CORBA::ORB_init(argc, argv);

        // 2. Récupération du RootPOA et activation du POA Manager 
        CORBA::Object_var poaObj = orb->resolve_initial_references("RootPOA");
        PortableServer::POA_var rootPoa = PortableServer::POA::_narrow(poaObj);
        
        PortableServer::POAManager_var poaManager = rootPoa->the_POAManager();
        poaManager->activate(); // ⚡ Activation essentielle pour traiter les requêtes !

        // 3. Instanciation du Servant 
        CalculateurImpl* calcImpl = new CalculateurImpl();
        CalculApp::Calculateur_var calcRef = calcImpl->_this();

        // 4. Connexion au Service de Nommage 🔍
        CORBA::Object_var nsObj = orb->resolve_initial_references("NameService");
        CosNaming::NamingContext_var nc = CosNaming::NamingContext::_narrow(nsObj);

        // Enregistrement de la référence sous le nom "Calculateur"
        CosNaming::Name name;
        name.length(1);
        name[0].id = CORBA::string_dup("CalculateurService");
        nc->rebind(name, calcRef);

        std::cout << "Serveur CORBA C++ prêt et en attente de requêtes..." << std::endl;

        // 5. Lancement de la boucle d'écoute de l'ORB 
        orb->run();

    } catch (const CORBA::Exception& e) {
        std::cerr << "Erreur CORBA Système dans le serveur !" << std::endl;
    }

    return 0;
}