# Maquettes UX + Cahier des charges technique
## Projet DIGITRANS-CM — Module CRM

### Pages maquettées
- Login / Authentification
- Dashboard analytique (KPI, graphiques)
- Liste et fiche client
- Formulaire commande
- Programme de fidélisation

### Stack technique validée
- Frontend : Angular 17 standalone + TailwindCSS
- Backend : Spring Boot 3.2 + Spring Security JWT
- Base de données : MySQL 8.0 (on-premise Douala)
- Cloud : AWS EC2 t2.micro (af-south-1)
- CI/CD : GitHub Actions → GHCR → EC2

### Contraintes
- Loi camerounaise 2010/012 : données clients hébergées localement
- Offline-first : 73% des fonctionnalités disponibles sans réseau
