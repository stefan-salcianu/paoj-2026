# Sistem de Management pentru o Companie Aeriană

**Materie:** Programare Avansată pe Obiecte (Java) — Etapa I  
**Pachet principal:** `com.pao.proiect.airline`

---

## 8 Tipuri de obiecte

| Nr. | Clasă      | Descriere                                                                  |
|-----|------------|----------------------------------------------------------------------------|
| 1   | `Persoana` | Clasă **abstractă**; rădăcina ierarhiei; metodă abstractă `getRol()`      |
| 2   | `Angajat`  | Extinde `Persoana`; adaugă salariu și departament                         |
| 3   | `Pilot`    | Extinde `Angajat`; adaugă licență și ore de zbor (2 niveluri de moștenire)|
| 4   | `Pasager`  | Extinde `Persoana`; adaugă pașaport și naționalitate                      |
| 5   | `Aeroport` | Entitate de infrastructură cu cod IATA, oraș și țară                      |
| 6   | `Avion`    | Aeronavă cu număr de înregistrare și capacitate de pasageri               |
| 7   | `Zbor`     | Zbor programat; implementează `Comparable<Zbor>` (sortare după dată)      |
| 8   | `Bilet`    | Rezervare **imutabilă** (câmpuri `final`, fără setteri, clasă `final`)    |

---

## 10 Acțiuni ale sistemului

| Nr. | Acțiune                            | Serviciu responsabil |
|-----|------------------------------------|----------------------|
| 1   | Adăugare aeroport nou              | `AeroportService`    |
| 2   | Adăugare avion nou                 | `AeroportService`    |
| 3   | Creare zbor între două aeroporturi | `ZborService`        |
| 4   | Înregistrare pasager               | `RezervareService`   |
| 5   | Adăugare pilot în sistem           | `AeroportService`    |
| 6   | Alocare pilot la un zbor           | `ZborService`        |
| 7   | Rezervare bilet pentru un pasager  | `RezervareService`   |
| 8   | Căutare zbor după destinație       | `ZborService`        |
| 9   | Afișare pasageri ai unui zbor      | `ZborService`        |
| 10  | Anulare bilet / rezervare          | `RezervareService`   |

---

## Structura pachetelor

```
src/com/pao/proiect/airline/
├── model/
│   ├── Persoana.java          (abstractă)
│   ├── Angajat.java           (extinde Persoana)
│   ├── Pilot.java             (extinde Angajat)
│   ├── Pasager.java           (extinde Persoana)
│   ├── Aeroport.java
│   ├── Avion.java
│   ├── StatusZbor.java        (enum: PROGRAMAT, INTARZIAT, ANULAT, FINALIZAT)
│   ├── Zbor.java              (implements Comparable<Zbor>)
│   └── Bilet.java             (imutabilă)
├── exception/
│   ├── ZborIndisponibilException.java   (checked, extends Exception)
│   └── EntitateNegasitaException.java   (unchecked, extends RuntimeException)
├── service/
│   ├── AeroportService.java   (Singleton — gestionează Aeroport, Avion, Pilot)
│   ├── ZborService.java       (Singleton — gestionează Zbor + pasageri per zbor)
│   └── RezervareService.java  (Singleton — gestionează Pasager și Bilet)
└── Main.java
```

---

## Cerințe OOP acoperite

- **Ierarhie 2 niveluri:** `Persoana` → `Angajat` → `Pilot`
- **Clasă abstractă:** `Persoana` cu `getRol()` abstract
- **Clasă imutabilă:** `Bilet` (câmpuri `final`, fără setteri, `final class`)
- **Singleton:** toate cele 3 servicii
- **Colecții:** `List`, `Set` (`HashSet`), `TreeSet` (sortată), `Map` (`HashMap`)
- **Colecție sortată:** `TreeSet<Zbor>` ordonată după `dataPlecare`
- **Map indexare:** `Map<String, Zbor>`, `Map<String, List<Pasager>>`
- **Excepții custom:** `ZborIndisponibilException` (checked), `EntitateNegasitaException` (unchecked)
- **`toString()` / `equals()` / `hashCode()`** suprascrise în `Persoana`, `Pasager`, `Pilot`, `Aeroport`, `Avion`, `Zbor`, `Bilet`
