# Plan de tests — API Gestion des événements et des inscriptions

**URL de base** : `http://localhost:8082`

**Convention** : `{token_admin}` et `{token_user}` désignent les valeurs `accessToken` récupérées lors des connexions ci-dessous. `{id}` désigne un identifiant réel à récupérer dans la réponse d'une requête précédente.

---

## 1. Authentification

### 1.1 Créer un compte (inscription publique)
```
POST /api/auth/register
```
```json
{
  "nom": "Ben Salah",
  "prenom": "Ahmed",
  "email": "ahmed@test.com",
  "motDePasse": "azerty123"
}
```
**Attendu** : `201 Created`, un email de vérification est envoyé, le mot de passe n'apparaît jamais dans la réponse.

### 1.2 Créer un compte avec un email déjà utilisé
Même requête que 1.1, avec le même email.
**Attendu** : `409 Conflict` — `"Cette adresse email est déjà utilisée"`.

### 1.3 Créer un compte avec des données invalides
```json
{
  "nom": "",
  "prenom": "",
  "email": "pasunemail",
  "motDePasse": "123"
}
```
**Attendu** : `400 Bad Request`, avec un objet `erreursChamps` listant une erreur par champ invalide (nom, prénom, email, mot de passe).

### 1.4 Se connecter avant vérification de l'email (RG5)
```
POST /api/auth/login
```
```json
{ "email": "ahmed@test.com", "motDePasse": "azerty123" }
```
**Attendu** : refusé (l'email n'est pas encore vérifié). Message explicite demandant de vérifier l'email.

### 1.5 Vérifier l'email
Récupérer le token depuis le lien reçu par email, puis :
```
GET /api/auth/verify-email?token=xxxxx
```
**Attendu** : `200 OK`.

### 1.6 Se connecter après vérification
```
POST /api/auth/login
```
```json
{ "email": "ahmed@test.com", "motDePasse": "azerty123" }
```
**Attendu** : `200 OK`, renvoie `accessToken`, `type: "Bearer"`, `expiresIn`, `email`, `role: "USER"`. → conserver ce token comme `{token_user}`.

### 1.7 Se connecter avec un mauvais mot de passe
```json
{ "email": "ahmed@test.com", "motDePasse": "mauvais" }
```
**Attendu** : `401 Unauthorized` — `"Email ou mot de passe incorrect"`.

### 1.8 Se connecter en tant qu'organisateur (admin)
```json
{ "email": "admin@projet-backend.tn", "motDePasse": "Admin@123" }
```
**Attendu** : `200 OK`, `role: "ADMIN"`. → conserver ce token comme `{token_admin}`.

### 1.9 Renvoyer un email de vérification
```
POST /api/auth/resend-verification
```
```json
{ "email": "ahmed@test.com" }
```
**Attendu** : `200 OK` si l'email n'était pas encore vérifié ; erreur si déjà vérifié.

---

## 2. Catégories

### 2.1 Consulter les catégories sans être connecté
```
GET /api/categories
```
(sans header Authorization)
**Attendu** : `401 Unauthorized` (consultation réservée aux utilisateurs connectés).

### 2.2 Consulter les catégories connecté
```
GET /api/categories
Authorization: Bearer {token_user}
```
**Attendu** : `200 OK`, liste des catégories.

### 2.3 Ajouter une catégorie en tant que participant (rôle insuffisant)
```
POST /api/categories
Authorization: Bearer {token_user}
```
```json
{ "nom": "Test", "description": "Test" }
```
**Attendu** : `403 Forbidden`.

### 2.4 Ajouter une catégorie en tant qu'organisateur
```
POST /api/categories
Authorization: Bearer {token_admin}
```
```json
{ "nom": "Conférence", "description": "Événements de présentation" }
```
**Attendu** : `201 Created`, catégorie créée avec un `id`. → noter cet id comme `{id_categorie}`.

### 2.5 Modifier une catégorie
```
PUT /api/categories/{id_categorie}
Authorization: Bearer {token_admin}
```
```json
{ "nom": "Conférence", "description": "Description mise à jour" }
```
**Attendu** : `200 OK`.

### 2.6 Supprimer une catégorie utilisée par un événement (RG3)
```
DELETE /api/categories/{id_categorie}
Authorization: Bearer {token_admin}
```
(après avoir créé un événement lié à cette catégorie — voir section 3)
**Attendu** : `409 Conflict` — `"Cette catégorie est encore utilisée par au moins un événement et ne peut pas être supprimée"`.

### 2.7 Supprimer une catégorie non utilisée
Créer une catégorie vide (sans événement associé), puis :
```
DELETE /api/categories/{id_categorie_vide}
Authorization: Bearer {token_admin}
```
**Attendu** : `204 No Content`.

---

## 3. Événements

### 3.1 Ajouter un événement
```
POST /api/evenements
Authorization: Bearer {token_admin}
```
```json
{
  "titre": "Introduction à Spring Boot",
  "description": "Découverte des bases du framework",
  "date": "2026-10-15T14:00:00",
  "lieu": "Amphi A",
  "capacite": 2,
  "actif": true,
  "prix": 20.0,
  "categorieId": {id_categorie}
}
```
**Attendu** : `201 Created`. Réponse inclut `placesDisponibles: 2`. → noter l'id comme `{id_evenement}`.

### 3.2 Consulter la liste des événements
```
GET /api/evenements
Authorization: Bearer {token_user}
```
**Attendu** : `200 OK`, chaque événement affiche `titre`, `categorieNom`, `date`, `lieu`, `capacite`, `placesDisponibles`.

### 3.3 Consulter le détail d'un événement
```
GET /api/evenements/{id_evenement}
Authorization: Bearer {token_user}
```
**Attendu** : `200 OK`.

### 3.4 Rechercher par titre
```
GET /api/evenements/recherche?titre=spring
Authorization: Bearer {token_user}
```
**Attendu** : `200 OK`, ne renvoie que les événements dont le titre contient "spring" (insensible à la casse).

### 3.5 Filtrer par catégorie
```
GET /api/evenements/categorie/{id_categorie}
Authorization: Bearer {token_user}
```
**Attendu** : `200 OK`, uniquement les événements de cette catégorie.

### 3.6 Filtrer par date
```
GET /api/evenements/date?debut=2026-10-01T00:00:00&fin=2026-10-31T23:59:59
Authorization: Bearer {token_user}
```
**Attendu** : `200 OK`, uniquement les événements d'octobre 2026.

### 3.7 Modifier un événement en tant que participant (rôle insuffisant)
```
PUT /api/evenements/{id_evenement}
Authorization: Bearer {token_user}
```
**Attendu** : `403 Forbidden`.

### 3.8 Supprimer un événement ayant des inscriptions (RG4)
(après avoir créé une inscription dessus — voir section 4)
```
DELETE /api/evenements/{id_evenement}
Authorization: Bearer {token_admin}
```
**Attendu** : `409 Conflict`.

---

## 4. Inscriptions

### 4.1 S'inscrire à un événement
```
POST /api/inscriptions
Authorization: Bearer {token_user}
```
```json
{ "evenementId": {id_evenement} }
```
**Attendu** : `201 Created`, état initial `EN_ATTENTE`. L'utilisateur est déduit automatiquement du token (pas besoin de le préciser). → noter l'id comme `{id_inscription}`.

### 4.2 S'inscrire une 2e fois au même événement (RG1)
Même requête que 4.1, avec le même utilisateur.
**Attendu** : `409 Conflict` (doublon d'inscription active refusé).

### 4.3 Capacité atteinte (RG2)
Créer un 2e compte participant, le vérifier, se connecter (`{token_user2}`), puis :
```
POST /api/inscriptions
Authorization: Bearer {token_user2}
```
```json
{ "evenementId": {id_evenement} }
```
(l'événement de test a `capacite: 2` ; répéter avec un 3e utilisateur si besoin pour dépasser)
**Attendu** : `409 Conflict` une fois la capacité atteinte.

### 4.4 Consulter mes inscriptions
```
GET /api/inscriptions/mes-inscriptions
Authorization: Bearer {token_user}
```
**Attendu** : `200 OK`, uniquement les inscriptions de l'utilisateur connecté.

### 4.5 Consulter toutes les inscriptions en tant que participant (rôle insuffisant)
```
GET /api/inscriptions
Authorization: Bearer {token_user}
```
**Attendu** : `403 Forbidden` (réservé à l'organisateur).

### 4.6 Consulter toutes les inscriptions en tant qu'organisateur
```
GET /api/inscriptions
Authorization: Bearer {token_admin}
```
**Attendu** : `200 OK`, liste complète avec participant, événement, date.

### 4.7 Annuler l'inscription d'un autre participant (sécurité)
Avec `{token_user2}`, essayer :
```
PUT /api/inscriptions/{id_inscription}/annuler
Authorization: Bearer {token_user2}
```
(où `{id_inscription}` appartient à l'utilisateur 1, pas 2)
**Attendu** : `403 Forbidden` — `"Vous ne pouvez annuler que vos propres inscriptions"`.

### 4.8 Annuler sa propre inscription
```
PUT /api/inscriptions/{id_inscription}/annuler
Authorization: Bearer {token_user}
```
**Attendu** : `200 OK`, état passé à `ANNULEE`.

### 4.9 Changer l'état en tant que participant (rôle insuffisant)
```
PUT /api/inscriptions/{id_inscription}/etat
Authorization: Bearer {token_user}
```
```json
{ "etat": "CONFIRMEE" }
```
**Attendu** : `403 Forbidden` (réservé à l'organisateur).

### 4.10 Transition d'état invalide (RG7)
Avec une inscription à l'état `EN_ATTENTE` :
```
PUT /api/inscriptions/{id_inscription}/etat
Authorization: Bearer {token_admin}
```
```json
{ "etat": "TERMINEE" }
```
**Attendu** : `409 Conflict` (on ne peut pas passer directement de `EN_ATTENTE` à `TERMINEE`).

### 4.11 Transition d'état valide
```json
{ "etat": "CONFIRMEE" }
```
**Attendu** : `200 OK`.

### 4.12 Retour en arrière interdit (RG7)
Sur la même inscription (maintenant `CONFIRMEE`) :
```json
{ "etat": "EN_ATTENTE" }
```
**Attendu** : `409 Conflict`.

---

## 5. Gestion des utilisateurs (organisateur uniquement)

### 5.1 Lister les utilisateurs en tant que participant (rôle insuffisant)
```
GET /api/utilisateurs
Authorization: Bearer {token_user}
```
**Attendu** : `403 Forbidden`.

### 5.2 Lister les utilisateurs en tant qu'organisateur
```
GET /api/utilisateurs
Authorization: Bearer {token_admin}
```
**Attendu** : `200 OK`, liste de tous les comptes (sans mot de passe).

### 5.3 Modifier un utilisateur
```
PUT /api/utilisateurs/{id_utilisateur}
Authorization: Bearer {token_admin}
```
```json
{ "nom": "Ben Salah", "prenom": "Ahmed", "email": "ahmed@test.com" }
```
**Attendu** : `200 OK`.

---

## 6. Sécurité générale

### 6.1 Accès à une route protégée sans token
```
GET /api/evenements
```
(sans header Authorization)
**Attendu** : `401 Unauthorized`.

### 6.2 Accès avec un token expiré ou invalide
```
GET /api/evenements
Authorization: Bearer token_invalide_ou_expire
```
**Attendu** : `401 Unauthorized`.

### 6.3 Déconnexion (côté frontend)
Non applicable côté API — le JWT étant stateless, la "déconnexion" consiste à supprimer le token côté client (pas d'appel serveur nécessaire).

---

## Récapitulatif des règles métier testées

| Règle | Description | Tests correspondants |
|---|---|---|
| RG1 | Pas de double inscription active | 4.2 |
| RG2 | Capacité maximale respectée | 4.3 |
| RG3 | Catégorie utilisée non supprimable | 2.6 |
| RG4 | Événement avec inscriptions non supprimable | 3.8 |
| RG5 | Email vérifié obligatoire avant connexion | 1.4, 1.5, 1.6 |
| RG6 | Contrôle d'accès par rôle | 2.3, 3.7, 4.5, 4.9, 5.1 |
| RG7 | Transitions d'état contrôlées | 4.10, 4.11, 4.12 |

## Comptes utilisés pour la démonstration

| Rôle | Email | Mot de passe |
|---|---|---|
| Organisateur (ADMIN) | admin@projet-backend.tn | Admin@123 |
| Participant 1 (USER) | ahmed@test.com | azerty123 |
| Participant 2 (USER) | (compte créé au test 4.3) | azerty123 |
