# Projet DIGITRANS-CM : Module CRM "SavoirManger"

![CAMTECH SOLUTIONS](https://img.shields.io/badge/Entreprise-CAMTECH%20SOLUTIONS%20S.A.-blue)
![Client](https://img.shields.io/badge/Client-AGROCAM%20S.A.-green)
![Module](https://img.shields.io/badge/Module-CRM%20SavoirManger-orange)

## Contexte du Projet

Ce dépôt contient le code source du **Module CRM** développé dans le cadre du projet **DIGITRANS-CM** (Digitalisation et Transformation Numérique au Cameroun) pour le groupe agroalimentaire **AGROCAM S.A.**, réalisé par l'ESN **CAMTECH SOLUTIONS S.A**.

Le projet global vise à moderniser l'intégralité du Système d'Information d'AGROCAM S.A. Ce module CRM est spécifiquement dédié à la **gestion de la relation client de la chaîne de restauration rapide "SavoirManger"**, présente à Douala, Yaoundé, Bafoussam, Garoua et Ngaoundéré.

## Enjeux et Contraintes Techniques

Afin de répondre aux réalités du contexte technologique camerounais et aux exigences strictes de la Direction d'AGROCAM S.A., l'architecture de ce module a été pensée pour :

* **Gérer la latence réseau élevée** (150 à 250 ms) avec des mécanismes de cache et un déploiement cloud régionalisé.
* **Fonctionner en mode dégradé (Offline-first)** : Résilience face aux délestages et coupures internet fréquentes, essentielle pour la continuité d'activité des restaurants "SavoirManger".
* **Souveraineté des données** : Les données sensibles des clients sont protégées et hébergées conformément à la loi n°2010/012 du 21 décembre 2010 relative à la cybersécurité et à la cybercriminalité au Cameroun.

## Architecture et Stack Technique

Le projet adopte une architecture moderne découplée (Frontend / Backend) :

### Backend (`/crmBackend`)
Développé pour garantir performance et sécurité, avec des temps de réponse optimisés.
* **Langage** : Java 21
* **Framework** : Spring Boot 4.0.6
* **Sécurité** : Spring Security, OAuth2 Client, JWT (JSON Web Tokens)
* **Base de données** : MySQL (Production) & H2 (Développement/Tests) avec Flyway pour les migrations
* **Mapping et Utilitaires** : MapStruct, Lombok
* **Documentation API** : SpringDoc OpenAPI (Swagger)

### Frontend (`/crmFrontend`)
Interface utilisateur réactive et performante, avec Server-Side Rendering (SSR) pour optimiser les performances sur des connexions lentes.
* **Framework** : Angular 21 (avec support SSR)
* **Design & UI** : TailwindCSS 4, PostCSS
* **Tests** : Vitest, JSdom

## Référence de l'API (Backend)

L'API RESTful est documentée et structurée autour des contrôleurs suivants (Base URL: `http://localhost:8080/api/v1`) :

* **Authentification (`/auth`)**
    * `POST /login` : Authentification utilisateur et génération du token JWT.
* **Clients (`/clients`)**
    * `GET /` : Liste de tous les clients.
    * `GET /{id}` : Détails d'un client.
    * `POST /` : Création d'un nouveau client.
    * `PUT /{id}` : Mise à jour d'un client existant.
    * `DELETE /{id}` : Suppression d'un client.
* **Commandes (`/commandes`)**
    * `GET /` : Liste des commandes.
    * `GET /{id}` : Détails d'une commande.
    * `POST /` : Enregistrement d'une nouvelle commande.
* **Restaurants (`/restaurants`)**
    * `GET /` : Liste des restaurants "SavoirManger".
    * `GET /{id}` : Détails d'un restaurant spécifique.
    * `POST /`, `PUT /{id}`, `DELETE /{id}` : Gestion des établissements.
* **Fidélisation & Interactions**
    * `GET /fidelisation/client/{clientId}` : Suivi du programme de fidélité d'un client.
    * `GET /interactions/client/{clientId}` : Historique des interactions avec le CRM.
    * `POST /interactions` : Ajout d'une nouvelle interaction.
* **Dashboard (`/dashboard`)**
    * `GET /` : Récupération des statistiques globales (KPIs) pour le tableau de bord.

## Structure du Frontend

Le frontend Angular est organisé par fonctionnalités (Feature Modules) pour garantir la scalabilité :
* **`auth`** : Composants de connexion et gestion des guards.
* **`clients`** : Liste et formulaires de création/édition des clients.
* **`commandes`** : Suivi des commandes passées dans les restaurants.
* **`dashboard`** : Tableau de bord principal affichant les indicateurs clés.
* **`restaurants`** : Gestion des points de vente "SavoirManger".
* **`fidelisation` / `interactions`** : Suivi de la relation client et des points de fidélité.

## Installation et Démarrage Local

### Prérequis
* Java Development Kit (JDK) 21
* Node.js (v20+)
* npm (v10+)
* MySQL (Optionnel pour le dev si utilisation de H2)

### 1. Lancement du Backend
```bash
cd crmBackend
# Installation et exécution avec Maven Wrapper
./mvnw clean install
./mvnw spring-boot:run
```
L'API sera disponible sur `http://localhost:8080` (et Swagger UI sur `/swagger-ui.html`).

### 2. Lancement du Frontend
```bash
cd crmFrontend
# Installation des dépendances
npm install
# Lancement du serveur de développement Angular
npm run start
```
L'interface utilisateur sera accessible sur `http://localhost:4200`.

## Indicateurs de Performance (KPIs)
Ce module s'inscrit dans un suivi rigoureux des KPIs exigés par la Direction :
* Taux de couverture des tests (Objectif : ≥ 80%)
* Temps de déploiement CI/CD (Objectif : ≤ 15 min)
* Disponibilité en mode dégradé (Objectif : ≥ 70%)

## Équipe Projet
Développé par l'équipe dédiée de **CAMTECH SOLUTIONS S.A.** dans le cadre de l'épreuve "Manager les Projets Numériques" (Bloc BC02).
