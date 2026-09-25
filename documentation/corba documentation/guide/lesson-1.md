Pour apprendre à mettre en pratique CORBA, le plus simple est de réaliser un **exemple concret étape par étape** (par exemple en Java).

Voici comment mettre en place une communication client-serveur pas à pas.

---

## Étape 1 : Écrire le contrat d'interface (`.idl`)



Créez un fichier nommé **`Calculateur.idl`**. Ce fichier définit le contrat neutre.

```idl
module CalculApp {
    interface Calculateur {
        double ajouter(in double a, in double b);
    };
};

```

* **`module`** : Équivalent d'un `package` en Java ou d'un `namespace` en C++.
* **`interface`** : Définit les méthodes accessibles à distance.


* **`in`** : Indique que les paramètres `a` et `b` sont envoyés en entrée vers le serveur.

---

## Étape 2 : Compiler le fichier IDL



Le compilateur IDL génère le **Stub** (client) et le **Skeleton** (serveur).

Dans votre terminal (avec le SDK Java approprié ou un outil comme OpenORB / JacORB) :

```bash
idlj -fall Calculateur.idl

```

Cette commande génère un dossier `CalculApp` contenant les classes Java nécessaires (`_CalculateurStub.java`, `CalculateurPOA.java`, etc.).

---

## Étape 3 : Implémenter le Serveur (Le Servant)



On crée la classe qui hérite du **POA** généré pour écrire la vraie logique métier.

**`CalculateurImpl.java`**

```java
import CalculApp.CalculateurPOA;

public class CalculateurImpl extends CalculateurPOA {
    @Override
    public double ajouter(double a, double b) {
        System.out.println("Requête reçue : " + a + " + " + b);
        return a + b;
    }
}

```

Puis, on initialise le serveur, l'ORB et le service de nommage :

**`Serveur.java`**

```java
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.CosNaming.*;

public class Serveur {
    public static void main(String[] args) {
        try {
            // 1. Initialiser l'ORB
            ORB orb = ORB.init(args, null);

            // 2. Obtenir et activer le POA (Portable Object Adapter)
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // 3. Créer l'instance du Servant
            CalculateurImpl calculateur = new CalculateurImpl();

            // 4. Enregistrer l'objet auprès du service de nommage (Naming Service)
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calculateur);
            
            org.omg.CORBA.Object namingContextObj = orb.resolve_initial_references("NameService");
            NamingContextExt namingContext = NamingContextExtHelper.narrow(namingContextObj);

            NameComponent path[] = namingContext.to_name("CalculateurService");
            namingContext.rebind(path, ref);

            System.out.println("Serveur CORBA prêt et en attente...");

            // 5. Lancer l'écoute des requêtes
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

---

## Étape 4 : Développer le Client



Le client recherche l'objet distant dans l'annuaire (Naming Service) et utilise le **Stub** pour exécuter la méthode.

**`Client.java`**

```java
import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import CalculApp.*;

public class Client {
    public static void main(String[] args) {
        try {
            // 1. Initialiser l'ORB
            ORB orb = ORB.init(args, null);

            // 2. Récupérer le Service de Nommage
            org.omg.CORBA.Object namingContextObj = orb.resolve_initial_references("NameService");
            NamingContextExt namingContext = NamingContextExtHelper.narrow(namingContextObj);

            // 3. Chercher la référence de l'objet distant
            Calculateur calc = CalculateurHelper.narrow(namingContext.resolve_str("CalculateurService"));

            // 4. Appeler la méthode distante comme si elle était locale
            double resultat = calc.ajouter(12.5, 7.5);
            System.out.println("Résultat reçu du serveur : " + resultat);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

---

## Étape 5 : Exécution du système

Pour faire fonctionner l'ensemble :

1. **Démarrer le service de nommage** (ex: `orbd -ORBInitialPort 1050`).
2. **Lancer le serveur** (`java Serveur -ORBInitialPort 1050 -ORBInitialHost localhost`).
3. **Lancer le client** (`java Client -ORBInitialPort 1050 -ORBInitialHost localhost`).

Le client affichera : `Résultat reçu du serveur : 20.0`.