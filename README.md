# 🏥 Système de Gestion Hospitalière — Architecture Microservices

Mini-Projet Spring Boot / Spring Cloud · Année 2025–2026

---

## 📁 Structure du Projet

```
hospital-microservices/
├── eureka-server/          → Service Discovery (port 8761)
├── config-server/          → Configuration centralisée (port 8888)
├── api-gateway/            → Point d'entrée unique (port 8080)
├── patient-service/        → Gestion des patients (port 8081)
├── appointment-service/    → Gestion des rendez-vous (port 8082)
├── medical-record-service/ → Gestion des dossiers médicaux (port 8083)
└── docker-compose.yml      → Démarrage automatisé de tous les services
```

---

## ⚙️ Prérequis

- Java 17+
- Maven 3.8+
- (Optionnel) Docker & Docker Compose

---

## 🚀 ÉTAPES DE RÉALISATION

### ÉTAPE 1 — Préparer l'environnement

```bash
# Vérifier Java
java -version   # Doit afficher Java 17 ou supérieur

# Vérifier Maven
mvn -version
```

---

### ÉTAPE 2 — Importer le projet dans votre IDE

1. Ouvrir **IntelliJ IDEA** (recommandé) ou Eclipse
2. `File → Open` → Sélectionner le dossier `hospital-microservices`
3. Chaque sous-dossier est un **module Maven indépendant**
4. Attendre que Maven télécharge toutes les dépendances

---

### ÉTAPE 3 — Ordre de démarrage (IMPORTANT !)

Les services ont des dépendances entre eux. **Respecter cet ordre :**

```
① eureka-server       (doit être UP avant tout le monde)
② config-server       (doit trouver Eureka)
③ patient-service     (service autonome)
④ appointment-service (dépend de patient-service)
⑤ medical-record-service (dépend de patient-service)
⑥ api-gateway         (doit trouver tous les services dans Eureka)
```

**Pour démarrer chaque service :**
```bash
# Depuis le dossier de chaque service :
cd eureka-server
mvn spring-boot:run

# Dans un nouveau terminal :
cd config-server
mvn spring-boot:run

# etc.
```

---

### ÉTAPE 4 — Vérifier que tout fonctionne

| URL | Description |
|-----|-------------|
| http://localhost:8761 | Dashboard Eureka — voir les services enregistrés |
| http://localhost:8888/patient-service/default | Config du Patient Service |
| http://localhost:8080/api/patients | API via la Gateway |
| http://localhost:8081/h2-console | BDD H2 Patient (JDBC: `jdbc:h2:mem:patientdb`) |
| http://localhost:8082/h2-console | BDD H2 Appointments |
| http://localhost:8083/h2-console | BDD H2 Medical Records |

---

### ÉTAPE 5 — Tester l'API avec Postman ou curl

#### 5.1 Créer un patient
```bash
POST http://localhost:8080/api/patients
Content-Type: application/json

{
  "nom": "Benali",
  "prenom": "Ahmed",
  "dateNaissance": "1990-05-15",
  "email": "ahmed.benali@email.com",
  "numeroDeTelephone": "0661234567",
  "adresse": "123 Rue Mohammed V, Rabat"
}
```

#### 5.2 Lister tous les patients
```bash
GET http://localhost:8080/api/patients
```

#### 5.3 Créer un rendez-vous (vérifie l'existence du patient via Patient Service)
```bash
POST http://localhost:8080/api/appointments
Content-Type: application/json

{
  "patientId": 1,
  "dateHeure": "2026-03-15T10:00:00",
  "motif": "Consultation générale",
  "medecinNom": "Dr. Alami"
}
```

#### 5.4 Consulter les rendez-vous d'un patient
```bash
GET http://localhost:8080/api/appointments/patient/1
```

#### 5.5 Créer un dossier médical
```bash
POST http://localhost:8080/api/medical-records
Content-Type: application/json

{
  "patientId": 1,
  "groupeSanguin": "A+",
  "allergies": "Pénicilline",
  "antecedentsMedicaux": "Diabète type 2"
}
```

#### 5.6 Ajouter un diagnostic au dossier
```bash
POST http://localhost:8080/api/medical-records/1/diagnostics
Content-Type: application/json

{
  "titre": "Hypertension",
  "description": "Tension artérielle élevée",
  "medecin": "Dr. Alami",
  "traitement": "Amlodipine 5mg/jour"
}
```

#### 5.7 Consulter l'historique médical
```bash
GET http://localhost:8080/api/medical-records/patient/1
GET http://localhost:8080/api/medical-records/1/diagnostics
```

---

### ÉTAPE 6 — Tester la Résilience (Circuit Breaker)

#### Scénario : Arrêter le Patient Service et créer un RDV

```bash
# 1. Arrêter le patient-service (Ctrl+C dans son terminal)
# 2. Essayer de créer un rendez-vous :
POST http://localhost:8080/api/appointments
{ "patientId": 1, "dateHeure": "2026-03-15T10:00:00", "motif": "Test résilience" }

# Résultat attendu : réponse d'erreur contrôlée (pas un crash)
# Le Circuit Breaker s'active après 5 échecs consécutifs
# → Le fallback renvoie un message clair au lieu d'une exception technique
```

---

### ÉTAPE 7 — Démarrage avec Docker Compose (alternative)

```bash
# Depuis la racine du projet :
docker-compose up --build

# Attendre ~2 minutes que tous les services démarrent

# Arrêter :
docker-compose down
```

---

## 🏗️ Architecture et Concepts Clés

### Communication inter-services

```
Client → API Gateway (8080)
             ↓ (routing via Eureka)
    ┌────────┬────────────┬──────────────────┐
Patient    Appointment  Medical Record
Service    Service      Service
 (8081)     (8082)       (8083)
             ↓                ↓
         Patient          Patient
         Service          Service
         (Feign)          (Feign)
```

### Circuit Breaker - États

```
FERMÉ (normal) → trop d'erreurs → OUVERT (fallback)
                                        ↓ après 10s
                                   SEMI-OUVERT (test)
                                        ↓ si OK
                                      FERMÉ
```

---

## 📋 Endpoints Complets

### Patient Service (`/api/patients`)
| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/patients` | Liste tous les patients |
| GET | `/api/patients/{id}` | Détail d'un patient |
| POST | `/api/patients` | Créer un patient |
| PUT | `/api/patients/{id}` | Modifier un patient |
| DELETE | `/api/patients/{id}` | Supprimer un patient |
| GET | `/api/patients/{id}/exists` | Vérifier l'existence (interne) |

### Appointment Service (`/api/appointments`)
| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/appointments` | Tous les rendez-vous |
| GET | `/api/appointments/{id}` | Un rendez-vous |
| GET | `/api/appointments/patient/{id}` | RDV d'un patient |
| POST | `/api/appointments` | Créer un RDV |
| PATCH | `/api/appointments/{id}/annuler` | Annuler un RDV |
| PATCH | `/api/appointments/{id}/statut?statut=CONFIRME` | Changer le statut |

### Medical Record Service (`/api/medical-records`)
| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/medical-records` | Tous les dossiers |
| GET | `/api/medical-records/{id}` | Un dossier |
| GET | `/api/medical-records/patient/{id}` | Dossier d'un patient |
| POST | `/api/medical-records` | Créer un dossier |
| POST | `/api/medical-records/{id}/diagnostics` | Ajouter un diagnostic |
| GET | `/api/medical-records/{id}/diagnostics` | Historique diagnostics |

---

## 🔧 Technologies

| Rôle | Technologie |
|------|-------------|
| Framework | Spring Boot 3.2 |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Config centralisée | Spring Cloud Config |
| Appels inter-services | OpenFeign |
| Résilience | Resilience4j (Circuit Breaker, Retry, Timeout) |
| Base de données | H2 (dev) / MySQL (prod) |
| Build | Maven |
| Conteneurisation | Docker Compose |
