# Projekt Case Study – Wellness360
## Task Management System (Spring Boot REST API)

Dieses Projekt ist ein REST-basiertes Task-Management-System, das ich mit **Spring Boot** und **MySQL** entwickelt habe.  
Ziel war es, saubere Backend-Architektur, Business-Logik, Asynchronität und Best Practices im Java-Umfeld umzusetzen.

Jede Aufgabe wird über eine **UUID** eindeutig identifiziert.  
Zusätzlich aktualisiert ein **asynchroner Scheduler** den Status einer Aufgabe automatisch von **PENDING** auf **IN_PROGRESS**, wenn eine definierte Zeit überschritten wird.

---

## Funktionen & Features

### CRUD-Funktionalität
- Aufgaben erstellen, abrufen, aktualisieren und löschen
- Statusverwaltung über Enum:
    - PENDING
    - IN_PROGRESS
    - COMPLETED

### UUID als Primärschlüssel
- Verwendung von UUIDs für bessere Skalierbarkeit und Sicherheit
- Keine sequenziellen IDs

### Asynchroner Scheduler
- Automatische Statusänderung von PENDING → IN_PROGRESS
- Umsetzung mit Spring Scheduler / Async-Mechanismen

### Validierung & Fehlerbehandlung
- Eingabevalidierung mit `@Valid`, `@NotNull`, `@NotBlank`, `@Future`
- Zentrale Exception-Behandlung mit:
    - `@RestControllerAdvice`
    - `@ExceptionHandler`
- Eigene Exceptions (z. B. ungültige UUID, Task nicht gefunden)

### Saubere Architektur
- Klare Trennung der Verantwortlichkeiten:
    - Controller
    - Service
    - Repository
- Erweiterbares und wartbares Design

### Logging
- Mehrstufiges Logging:
    - INFO
    - DEBUG
    - ERROR
- Unterstützung für Debugging und Monitoring

### DTO-Konzept
- Einsatz des DTO-Patterns zur Entkopplung von Entity und API
- Schutz interner Felder
- Vorbereitung für spätere Refactorings
- Manuelles Mapping (MapStruct als Erweiterung geplant)

### Request Interceptor
- Abfangen von HTTP-Requests vor dem Controller
- Einsatz für Logging, Header-Manipulation und Vorbereitung auf Authentifizierung

### Lombok
- Reduzierung von Boilerplate-Code
- Bessere Lesbarkeit und Wartbarkeit

---

## Technologie-Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL (Produktion)
- H2 (Development)
- Maven
- Lombok
- Postman

---

## Architektur & Designentscheidungen

### Paketstruktur
- Umsetzung nach dem Package-by-Layer-Prinzip
- Trennung in:
    - Präsentationsschicht
    - Business-Logik
    - Datenzugriff
    - Infrastruktur

### Sicherheit & Datenkapselung
- Sensible Felder werden nicht im API-Response ausgegeben
- Nutzung von `@JsonIgnore`

### Statusverwaltung
- Task-Status als Enum für Typsicherheit
- Klar definierter Task-Lifecycle

### Profile & Umgebungen
- `dev` → H2 In-Memory-Datenbank
- `prod` → MySQL
- Umschaltung über Spring Profiles

---

## Projekt starten

### Voraussetzungen
- Java 17+
- Maven
- MySQL
- IDE (IntelliJ, Eclipse oder VS Code)
- Postman

### Anwendung starten
```bash
mvn clean install
mvn spring-boot:run
