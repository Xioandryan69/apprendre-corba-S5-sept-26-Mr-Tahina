#include <iostream>
#include "Calculateur.hh" // Fichier généré par l'IDL

int main(int argc, char* argv[]) {
    try {
        // 1. Initialisation de l'ORB 
        CORBA::ORB_var orb = CORBA::ORB_init(argc, argv);

        // 2. Récupération du Service de Nommage 
        CORBA::Object_var objRef = orb->resolve_initial_references("NameService");
        CosNaming::NamingContext_var nc = CosNaming::NamingContext::_narrow(objRef);

        // 3. Recherche du serveur "Calculateur" dans le service de nommage 
        CosNaming::Name name;
        name.length(1);
        name[0].id = CORBA::string_dup("CalculateurService");

        // Récupération de la référence et cast vers le bon type (Smart Pointer _var)
        CalculApp::Calculateur_var calc = CalculApp::Calculateur::_narrow(nc->resolve(name));

        // 4. Appel distant 
        CORBA::Double res = calc->ajouter(15.5, 4.5);
        std::cout << "Résultat de l'addition : " << res << std::endl;


        // 2. Création et remplissage de la séquence 
        CalculApp::TableauDeDoubles mesNombres;
        mesNombres.length(4); // Définition de la taille
        mesNombres[0] = 10.0;
        mesNombres[1] = 15.5;
        mesNombres[2] = 20.0;
        mesNombres[3] = 14.5;

        // 3. Appel de la méthode distante 
        std::cout << "Calcul de la moyenne de {10.0, 15.5, 20.0, 14.5}..." << std::endl;
        CORBA::Double moyenne = calc->calculerMoyenne(mesNombres);
        std::cout << "Moyenne reçue du serveur : " << moyenne << std::endl;

        
        // 4. Test d'une division par zéro 
        std::cout << "Tentative de division par zéro (10 / 0)..." << std::endl;
        CORBA::Double div= calc->division(10.0, 1.0);
        std::cout << "Résultat : " << div << std::endl;

    } catch (const CalculApp::DivisionParZero& e) {
        // Exception métier définie dans l'IDL 
        std::cerr << "Erreur métier : " << e.message << std::endl;
    } catch (const CORBA::Exception& e) {
        // Exception système/réseau CORBA 
        std::cerr << "Erreur CORBA Système" << std::endl;
    }

    return 0;
}