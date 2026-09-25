Votre document contient déjà une très bonne synthèse détaillée de CORBA ! Voici les notions fondamentales à retenir pour maîtriser l'essentiel du sujet :

---

## 1. Vue d'ensemble : Qu'est-ce que CORBA ?

**CORBA** (*Common Object Request Broker Architecture*) est une norme créée par l'**OMG** (*Object Management Group*). Son rôle est d'assurer l'interopérabilité entre des logiciels distribués : elle permet à un client d'appeler une méthode sur un objet serveur situé sur une autre machine, même s'ils sont écrits dans des **langages différents** (ex: Java et C++).

---

## 2. Le Modèle de Référence OMG

L'OMG découpe les interfaces d'un système distribué en 4 niveaux :

```
┌──────────────────────────────────────────────────────────────────┐
│                   Interfaces d'Application                       │
├─────────────────────────┬────────────────────────────────────────┤
│  Interfaces de Domaine  │           Fonctions Communes           │
├─────────────────────────┴────────────────────────────────────────┤
│                       Services d'Objets                          │
└──────────────────────────────────────────────────────────────────┘

```

1. **Services d'Objets (*Object Services*) :** Services système horizontaux fondamentaux.


* *Service de Nommage (Naming Service) :* Annuaire pour retrouver un objet par son nom.


* *Service de Trading (Trader Service) :* Recherche d'objets selon leurs critères ou fonctionnalités.


* *Autres :* Sécurité, gestion des transactions, notification d'événements.




2. **Fonctions Communes (*Common Facilities*) :** Interfaces horizontales orientées utilisateur (ex: intégration de documents composants).


3. **Interfaces de Domaine (*Domain Interfaces*) :** Standards verticaux pour un secteur d'activité (télécoms, finance, santé, fabrication).


4. **Interfaces d'Application (*Application Interfaces*) :** Code sur-mesure propre à une application (non standardisé par l'OMG).



---

## 3. Les Composants Internes de l'ORB

L'**ORB** (*Object Request Broker*) agit comme un bus de communication pour offrir une **transparence de localisation** (l'appel distant ressemble à un appel local).

```
  CLIENT                                                     SERVEUR
┌───────────────┐                                         ┌─────────┐
│     Client    │                                         │ Servant │
└───────┬───────┘                                         └────▲────┘
        │                                                      │
┌───────▼───────┬───────────────┐         ┌───────────────┬────┴────┐
│   Stub IDL    │      DII      │         │      DSI      │Skeleton │
├───────────────┴───────────────┤         ├───────────────┴─────────┤
│         Interface ORB         │         │  POA (Adaptateur Objet) │
└───────────────┬───────────────┘         └───────────────▲─────────┘
                │                                         │
┌───────────────▼─────────────────────────────────────────┴─────────┐
│                      Bus ORB (Protocole IIOP)                     │
└───────────────────────────────────────────────────────────────────┘

```

* **Fichier IDL (*Interface Definition Language*) :** Contrat d'interface neutre définissant les méthodes et types autorisés.
* **Servant :** L'implémentation concrète de l'objet dans le langage cible (ex: classe C++ ou Java).


* **Stub (Souche) :** Généré automatiquement par le compilateur IDL côté client. Il intercepte l'appel et réalise le **Marshaling** (conversion des paramètres en binaire CDR).


* **Skeleton (Squelette) :** Généré automatiquement côté serveur. Il reçoit le message binaire, effectue le **Demarshaling** (décodage) et appelle le Servant.


* **POA (*Portable Object Adapter*) :** Composant serveur faisant le lien entre l'ORB et les Servants. Il gère l'activation des objets et optimise l'utilisation de la mémoire RAM (ex: réutilisation d'une instance pour plusieurs identifiants).


* **DII / DSI (Interfaces Dynamiques) :**
* **DII (*Dynamic Invocation Interface*) :** Permet au client de fabriquer et d'émettre des requêtes à la volée sans connaître l'interface à la compilation.


* **DSI (*Dynamic Skeleton Interface*) :** Permet au serveur de traiter dynamiquement des requêtes sans Skeleton prédéterminé.





---

## 4. Protocole & Format de Données

* **Protocole :** **IIOP** (*Internet Inter-ORB Protocol*), version adaptée au réseau **TCP/IP**.
* **Format des données :** Encodage binaire **CDR** (*Common Data Representation*).
* *Avantage :* Très rapide et économe en bande passante.
* *Inconvénient :* Format binaire non lisible directement par un humain.



---

## 5. Bilan Synthetique

| Points Forts | Limites & Remplaçants Modernes |
| --- | --- |
| • Interopérabilité multi-langages

<br>

<br>• Transparence réseau

<br>

<br>• Bonnes performances (binaire) | • Complexité de configuration et couplage fort<br>

<br>• Difficile à faire passer à travers les pare-feux<br>

<br>• **Alternatives actuelles :** API REST (JSON/HTTP) ou gRPC (HTTP/2 + Protobuf) |