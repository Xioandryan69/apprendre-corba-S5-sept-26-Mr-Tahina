Parfait. 🎯 Ton environnement est **exactement ce qu'il faut pour un ancien projet CORBA**.

Tu as confirmé :

```text
Java       : 1.8.0_504
javac      : 1.8.0_504
idlj       : 3.2
CORBA ORB  : org/omg/CORBA/ORB.class
```

Donc **ne télécharge aucun `corba.jar`**. Dans ton Java 8, CORBA est déjà intégré dans `rt.jar`.

Le problème vient simplement du fait que **VS Code compile actuellement ton projet avec Java 21**.

---

# Étape 1 — Tester ton projet CORBA avec Java 8

Va dans ton projet :

```bash
cd "/home/huhu/S5/Mr Tahina/CORBA/24 sept/apprendre-corba-S5-sept-26-Mr-Tahina"
```

Regarde la structure :

```bash
find . -maxdepth 3 -type f | sort
```

Ensuite, teste directement la compilation avec Java 8.

Si tes `.java` sont dans `src` :

```bash
mkdir -p bin
```

Puis :

```bash
/usr/lib/jvm/java-8-openjdk-amd64/bin/javac -d bin $(find src -name "*.java")
```

### Si ça compile

Tu verras simplement revenir le prompt :

```text
huhu@huhu-GL552JX:~/...$
```

sans erreur.

Cela prouvera que :

```text
Java 8
   ↓
CORBA
   ↓
org.omg.CORBA
   ↓
_CalculateurStub
```

fonctionne correctement.

---

# Étape 2 — Configurer VS Code

Maintenant nous allons dire à **VS Code** :

> Pour ce projet CORBA, utilise Java 8.

Dans ton projet :

```bash
mkdir -p .vscode
```

Puis ouvre le fichier :

```bash
code .vscode/settings.json
```

Mets :

```json
{
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-1.8",
            "path": "/usr/lib/jvm/java-8-openjdk-amd64",
            "default": true
        },
        {
            "name": "JavaSE-21",
            "path": "/usr/lib/jvm/java-21-openjdk-amd64"
        }
    ]
}
```

### Important

Le `default: true` ici signifie :

> **Java 8 est utilisé par ce projet VS Code.**

Cela ne change pas définitivement ton Ubuntu.

Ton terminal global peut continuer à avoir :

```bash
java -version
```

qui donne :

```text
21.0.12
```

---

# Étape 3 — Recharge VS Code

Dans VS Code :

**`Ctrl + Shift + P`**

cherche :

```text
Java: Clean Java Language Server Workspace
```

Puis sélectionne :

```text
Restart and delete
```

VS Code va redémarrer son serveur Java.

Ensuite regarde ton fichier :

```text
_CalculateurStub.java
```

Les erreurs :

```text
org.omg cannot be resolved to a type
```

devraient disparaître.

---

# Étape 4 — Vérifier le JDK utilisé par VS Code

Dans VS Code :

**Ctrl + Shift + P**

puis :

```text
Java: Configure Java Runtime
```

Tu devrais voir quelque chose comme :

```text
Installed JDKs

JavaSE-1.8
/usr/lib/jvm/java-8-openjdk-amd64

JavaSE-21
/usr/lib/jvm/java-21-openjdk-amd64
```

Et pour ton projet CORBA :

```text
JavaSE-1.8
```

doit être sélectionné.

---

# Pourquoi ça fonctionne ?

Ton `_CalculateurStub.java` contient des classes de l'ancien modèle CORBA :

```java
org.omg.CORBA.ORB
```

```java
org.omg.CORBA.portable.ObjectImpl
```

```java
org.omg.CORBA.portable.InputStream
```

```java
org.omg.CORBA.portable.OutputStream
```

Java 8 possède ces classes directement dans :

```text
/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar
```

Tu viens d'ailleurs de le démontrer avec :

```text
org/omg/CORBA/ORB.class
```

Donc :

```text
Java 8
   │
   └── rt.jar
        │
        └── org.omg.CORBA
             │
             ├── ORB
             ├── Object
             ├── Any
             ├── TypeCode
             └── portable.*
```

Alors que Java 21 n'a plus ces packages dans son JDK.

---

# Et `idlj` est également présent

C'est **très important pour ton cours**.

Tu as :

```text
IDL-to-Java compiler (portable), version "3.2"
```

Donc tu peux faire par exemple :

```bash
idlj
```

pour transformer un fichier :

```text
Calculateur.idl
```

en classes Java CORBA :

```text
Calculateur.java
CalculateurHelper.java
CalculateurHolder.java
CalculateurOperations.java
_CalculateurStub.java
```

C'est probablement exactement le mécanisme utilisé dans ton TP.

---

## ⚠️ Mais je veux vérifier une chose avant qu'on continue

Ne modifie **pas** encore `_CalculateurStub.java`.

Fais simplement :

```bash
cd "/home/huhu/S5/Mr Tahina/CORBA/24 sept/apprendre-corba-S5-sept-26-Mr-Tahina"
```

puis :

```bash
find . -maxdepth 3 -type f | sort
```

**Envoie-moi le résultat.**

Je veux voir ton projet CORBA complet (`.idl`, `Client`, `Server`, `Stub`, `Skeleton`, etc.) afin de te montrer **la chaîne complète CORBA** :

```text
Calculateur.idl
      ↓
     idlj
      ↓
Classes Java
      ↓
      ├── Client
      ├── Server
      ├── Stub
      └── Skeleton/POA
      ↓
      ORB
      ↓
Communication Client ↔ Serveur
```

On pourra ensuite faire fonctionner ton exemple **avec Java 8 sans toucher à ton Java 21**.
