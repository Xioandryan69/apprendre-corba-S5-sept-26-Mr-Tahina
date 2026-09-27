# include <iostream>
# include "Calculateur.hh"

// Héritage du squelette POA généré par omniidl
class CalculateurImpl : public virtual POA_CalculApp::Calculateur {
public:
    CalculateurImpl() {}
    virtual ~CalculateurImpl() {}

    // 1. Addition simple 
    CORBA::Double ajouter(CORBA::Double a, CORBA::Double b) override {
        return a + b;
    }

    // 2. Soustraction 
    CORBA::Double soustraire(CORBA::Double a, CORBA::Double b) override {
        return a - b;
    }

    // 3. Division avec gestion de l'exception 
    CORBA::Double division(CORBA::Double a, CORBA::Double b) override {
        if (b == 0.0) {
            // On lève l'exception définie dans l'IDL
            throw CalculApp::DivisionParZero("Division par zero impossible !");
        }
        return a / b;
    }

    // 4. Calcul via une structure Operation 
    CORBA::Double calculer(const CalculApp::Operation& op) override {
        std::string typeOp =(std::string) op.typeOperation;
        if (typeOp == "+") return op.nombreA + op.nombreB;
        if (typeOp == "-") return op.nombreA - op.nombreB;
        if (typeOp == "/") return division(op.nombreA, op.nombreB);
        return 0.0;
    }

    // 5. Calcul de moyenne via un Tableau (sequence IDL) 
    CORBA::Double calculerMoyenne(const CalculApp::TableauDeDoubles& nombres) override {
        if (nombres.length() == 0) return 0.0;
        
        CORBA::Double somme = 0.0;
        for (CORBA::ULong i = 0; i < nombres.length(); i++) {
            somme += nombres[i];
        }
        return somme / nombres.length();
    }
};