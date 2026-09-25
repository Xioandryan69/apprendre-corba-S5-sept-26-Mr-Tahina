Oui. Maintenant que `idlj` a généré les fichiers Java, **ne les modifie pas**. La prochaine étape est de construire le petit système CORBA complet : **Implémentation serveur → Serveur → Client → exécution**.

On va le faire progressivement pour que tu comprennes ce qui se passe.

## 1. Vérifie d'abord les fichiers générés

Depuis la racine du projet :

```bash
cd "/home/huhu/S5/Mr Tahina/CORBA/24 sept/apprendre-corba-S5-sept-26-Mr-Tahina"
```

Puis :

```bash
find src -type f | sort
```

Tu devrais avoir quelque chose proche de :

```text
src/CalculApp/Calculateur.java
src/CalculApp/CalculateurHelper.java
src/CalculApp/CalculateurHolder.java
src/CalculApp/CalculateurOperations.java
src/CalculApp/CalculateurPOA.java
src/CalculApp/_CalculateurStub.java
```

Ces fichiers sont la **partie générée**.

---

# 2. Crée maintenant l'implémentation serveur

C'est **la première classe que tu dois écrire toi-même**.

Crée :

```text
src/CalculApp/CalculateurImpl.java
```

Commande :

```bash
nano src/CalculApp/CalculateurImpl.java
```

Mets :

```java
package CalculApp;

public class CalculateurImpl extends CalculateurPOA {

    @Override
    public double ajouter(double a, double b) {
        return a + b;
    }
}
```

### Comprends cette ligne

```java
public class CalculateurImpl extends CalculateurPOA
```

Tu dis :

> Mon objet métier est un objet CORBA utilisable par le serveur.

La relation est :

```text
CalculateurImpl
      │
      extends
      ▼
CalculateurPOA
      │
      ▼
CORBA
```

Et :

```java
public double ajouter(double a, double b) {
    return a + b;
}
```

est **ton vrai métier**.

Pour :

```text
10 + 5
```

le serveur fera réellement :

```text
15
```

---

# 3. Compile

Comme tu sais déjà que Java 8 fonctionne :

```bash
rm -rf bin
mkdir bin
```

Puis :

```bash
/usr/lib/jvm/java-8-openjdk-amd64/bin/javac -d bin $(find src -name "*.java")
```

Tu devrais avoir éventuellement le warning :

```text
IORCheckImpl is internal proprietary API
```

mais **pas d'erreur**.

---

# 4. Maintenant crée le serveur

Crée :

```text
src/Serveur.java
```

Commande :

```bash
nano src/Serveur.java
```

Pour un premier exercice, mets :

```java
import CalculApp.Calculateur;
import CalculApp.CalculateurHelper;
import CalculApp.CalculateurImpl;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Serveur {

    public static void main(String[] args) {

        try {

            // 1. Démarrer l'ORB
            ORB orb = ORB.init(args, null);

            // 2. Récupérer le POA
            POA rootPOA =
                POAHelper.narrow(
                    orb.resolve_initial_references("RootPOA")
                );

            // 3. Créer l'objet métier
            CalculateurImpl calculateurImpl =
                new CalculateurImpl();

            // 4. Transformer l'objet Java en objet CORBA
            org.omg.CORBA.Object ref =
                rootPOA.servant_to_reference(calculateurImpl);

            // 5. Transformer la référence CORBA
            //    en interface Calculateur
            Calculateur calculateur =
                CalculateurHelper.narrow(ref);

            // 6. Afficher la référence CORBA
            String ior =
                orb.object_to_string(calculateur);

            System.out.println("IOR =");
            System.out.println(ior);

            // 7. Activer le POA
            rootPOA.the_POAManager().activate();

            // 8. Le serveur attend les requêtes
            System.out.println("Serveur CORBA démarré...");

            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

# 5. Comprends le démarrage du serveur

Il se passe ceci :

```text
Serveur.java
     │
     │ ORB.init()
     ▼
    ORB
     │
     │ RootPOA
     ▼
    POA
     │
     │ servant_to_reference()
     ▼
CalculateurImpl
     │
     ▼
Référence CORBA
     │
     ▼
    IOR
```

---

# 6. Compile encore

```bash
/usr/lib/jvm/java-8-openjdk-amd64/bin/javac -d bin $(find src -name "*.java")
```

Si aucune erreur :

```text
✅ compilation réussie
```

---

# 7. Lance le serveur

Avec Java 8 :

```bash
/usr/lib/jvm/java-8-openjdk-amd64/bin/java -cp bin Serveur
```

Tu devrais obtenir quelque chose comme :

```text
IOR =
IOR:000000000000...
Serveur CORBA démarré...
```

⚠️ **Ne ferme pas ce terminal.**

Le serveur doit rester actif parce que :

```java
orb.run();
```

signifie essentiellement :

> ORB, reste maintenant en attente des requêtes des clients.

Ton terminal sera donc « bloqué ». **C'est normal.**

---

# 8. Pourquoi afficher l'IOR ?

Cette ligne :

```java
String ior = orb.object_to_string(calculateur);
```

transforme la référence CORBA en chaîne.

Tu obtiens :

```text
IOR:000000...
```

Cette chaîne permet au client de retrouver l'objet distant.

C'est donc notre premier moyen de faire :

```text
CLIENT
   │
   │ IOR
   ▼
SERVEUR
```

---

# 9. Ensuite seulement : le client

Quand le serveur fonctionne, ouvre **un deuxième terminal**.

Ne tue pas le serveur.

Dans le deuxième terminal, on créera :

```text
src/Client.java
```

Le client fera essentiellement :

```text
1. démarrer ORB
2. récupérer l'IOR
3. string_to_object()
4. CalculateurHelper.narrow()
5. calculateur.ajouter(10, 5)
6. afficher 15
```

Le chemin complet deviendra :

```text
                SERVEUR
                   │
            CalculateurImpl
                   │
                   ▼
                  POA
                   │
                   ▼
                  ORB
                   │
                 réseau
                   │
                   ▼
                  ORB
                   │
                   ▼
                Client
                   │
                   ▼
              _CalculateurStub
                   │
                   ▼
        calculateur.ajouter(10,5)
                   │
                   ▼
                  15
```

---

## ⚠️ Une petite correction par rapport à ton apprentissage

Pour l'instant, **ne cherche pas à comprendre `Helper`, `Stub`, `POA`, `Delegate` en exécutant tout d'un coup**.

Tu as déjà étudié :

```text
Helper
Stub
POA
```

Maintenant on va les voir **en action**.

### Ordre pédagogique

```text
Étape 1 ✅
Calculateur.idl
       ↓
idlj
       ↓
fichiers Java générés

Étape 2 ← NOUS SOMMES ICI
       ↓
CalculateurImpl

Étape 3
       ↓
Serveur

Étape 4
       ↓
IOR

Étape 5
       ↓
Client

Étape 6
       ↓
appel distant :
calculateur.ajouter(10,5)

Étape 7
       ↓
résultat : 15
```

**Commence uniquement par `CalculateurImpl.java`, compile, puis crée `Serveur.java`.** Si tu me montres ensuite la sortie de la compilation ou l'IOR, on pourra faire le **Client CORBA ligne par ligne** et suivre réellement la requête de `10 + 5` jusqu'au serveur.
