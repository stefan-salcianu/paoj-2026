# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Airline Management System — a pure Java console application (no frameworks) for a university PAO (Advanced OOP) course. Demonstrates OOP principles: inheritance, immutability, Singleton services, custom exceptions, and Java Collections.

## Build & Run

This is an IntelliJ IDEA project with no Maven/Gradle. Compile and run manually from the `src/` directory:

```bash
cd src

# Compile
javac -d out com/pao/proiect/airline/model/*.java \
             com/pao/proiect/airline/exception/*.java \
             com/pao/proiect/airline/service/*.java \
             com/pao/proiect/airline/Main.java

# Run
java -cp out com.pao.proiect.airline.Main
```

Or use IntelliJ's built-in run (the `.iml` file at the repo root configures the source root as `src/`).

## Architecture

### Packages

- **`model/`** — 8 entity classes (POJOs + enum)
- **`service/`** — 3 Singleton services holding all business logic and in-memory state
- **`exception/`** — 2 custom exceptions
- **`Main.java`** — entry point; acts as integration demo, sequentially exercises all 10 operations

### Model Hierarchy

```
Persoana (abstract — defines getRol())
├── Angajat  →  Pilot        (2-level inheritance)
└── Pasager
```

`Zbor` implements `Comparable<Zbor>` — primary sort by `dataPlecare`, secondary by `idZbor`, so `TreeSet<Zbor>` always holds chronologically ordered flights.

`Bilet` is **immutable**: all fields are `final`, there are no setters, and the class is `final`. `dataRezervare` is set via `LocalDateTime.now()` in the constructor.

### Service Layer (Singletons)

| Service | Responsibility | Key collections |
|---|---|---|
| `AeroportService` | Airports, aircraft, pilots | `HashMap<String, Aeroport>`, `HashMap<String, Avion>`, `HashMap<String, Pilot>` |
| `ZborService` | Flights, pilot assignment, passenger lists per flight | `TreeSet<Zbor>` (sorted) + `HashMap<String, Zbor>` (fast lookup) + `Map<String, List<Pasager>>` |
| `RezervareService` | Passenger registry, ticket booking/cancellation | `HashMap<String, Pasager>`, `List<Bilet>`, `HashSet<String>` bilet IDs |

All three are lazy-initialized singletons accessed via `getInstance()`.

### Exception Strategy

- `ZborIndisponibilException` — **checked**; thrown when a flight is cancelled or fully booked. Must be caught at call sites of `RezervareService.rezervaBilet()`.
- `EntitateNegasitaException` — **unchecked** (`RuntimeException`); thrown across all services when an entity is not found by ID.

### Seat Management

`Zbor.locuriDisponibile` is initialised from `Avion.capacitate`. `RezervareService` decrements it on booking and increments it on cancellation; overbooking throws `ZborIndisponibilException`.

## Conventions

- All class/method/field names are in **Romanian**. Keep new code consistent with this convention.
- `equals`/`hashCode` are overridden on business keys: `Pilot` uses `licenta`, `Pasager` uses `pasaportId`, `Aeroport` uses `cod`, `Avion` uses `numarInregistrare`, `Zbor` uses `idZbor`, `Bilet` uses `idBilet`.
- There are no unit tests; `Main.java` is the only runnable demo. New features should be demonstrated there.
