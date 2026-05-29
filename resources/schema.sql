
DROP TABLE IF EXISTS bilete;
DROP TABLE IF EXISTS zboruri;
DROP TABLE IF EXISTS piloti;
DROP TABLE IF EXISTS pasageri;
DROP TABLE IF EXISTS avioane;
DROP TABLE IF EXISTS aeroporturi;

CREATE TABLE aeroporturi (
    cod   VARCHAR(10)  PRIMARY KEY,
    nume  VARCHAR(100) NOT NULL,
    oras  VARCHAR(100) NOT NULL,
    tara  VARCHAR(100) NOT NULL
);


CREATE TABLE avioane (
    id                 VARCHAR(20)  PRIMARY KEY,
    model              VARCHAR(100) NOT NULL,
    numar_inregistrare VARCHAR(50)  NOT NULL UNIQUE,
    capacitate         INT          NOT NULL CHECK (capacitate > 0)
);

CREATE TABLE piloti (
    id          VARCHAR(20)   PRIMARY KEY,
    nume        VARCHAR(100)  NOT NULL,
    prenume     VARCHAR(100)  NOT NULL,
    email       VARCHAR(150),
    angajat_id  VARCHAR(20)   NOT NULL,
    salariu     DECIMAL(10,2) NOT NULL CHECK (salariu >= 0),
    departament VARCHAR(100)  NOT NULL DEFAULT 'Operatiuni Zboruri',
    licenta     VARCHAR(50)   NOT NULL UNIQUE,
    ore_de_zbor INT           NOT NULL DEFAULT 0 CHECK (ore_de_zbor >= 0)
);

CREATE TABLE pasageri (
    id            VARCHAR(20)  PRIMARY KEY,
    nume          VARCHAR(100) NOT NULL,
    prenume       VARCHAR(100) NOT NULL,
    email         VARCHAR(150),
    pasaport_id   VARCHAR(50)  NOT NULL UNIQUE,
    nationalitate VARCHAR(100) NOT NULL
);




CREATE TABLE zboruri (
    id_zbor              VARCHAR(20) PRIMARY KEY,
    aeroport_plecare_cod VARCHAR(10) NOT NULL,
    aeroport_sosire_cod  VARCHAR(10) NOT NULL,
    data_plecare         TIMESTAMP   NOT NULL,
    data_sosire          TIMESTAMP   NOT NULL,
    avion_id             VARCHAR(20) NOT NULL,
    pilot_id             VARCHAR(20),
    status               VARCHAR(20) NOT NULL DEFAULT 'PROGRAMAT',
    locuri_disponibile   INT         NOT NULL CHECK (locuri_disponibile >= 0),

    CONSTRAINT fk_zbor_plecare FOREIGN KEY (aeroport_plecare_cod) REFERENCES aeroporturi(cod),
    CONSTRAINT fk_zbor_sosire  FOREIGN KEY (aeroport_sosire_cod)  REFERENCES aeroporturi(cod),
    CONSTRAINT fk_zbor_avion   FOREIGN KEY (avion_id)             REFERENCES avioane(id),
    CONSTRAINT fk_zbor_pilot   FOREIGN KEY (pilot_id)             REFERENCES piloti(id),

    CONSTRAINT chk_zbor_status CHECK (status IN ('PROGRAMAT', 'INTARZIAT', 'ANULAT', 'FINALIZAT')),
    CONSTRAINT chk_zbor_ruta   CHECK (aeroport_plecare_cod <> aeroport_sosire_cod),
    CONSTRAINT chk_zbor_durata CHECK (data_sosire > data_plecare)
);


CREATE TABLE bilete (
    id_bilet       VARCHAR(20)   PRIMARY KEY,
    pasager_id     VARCHAR(20)   NOT NULL,
    zbor_id        VARCHAR(20)   NOT NULL,
    clasa          VARCHAR(50)   NOT NULL,
    pret           DECIMAL(10,2) NOT NULL CHECK (pret >= 0),
    data_rezervare TIMESTAMP     NOT NULL,

    CONSTRAINT fk_bilet_pasager FOREIGN KEY (pasager_id) REFERENCES pasageri(id),
    CONSTRAINT fk_bilet_zbor    FOREIGN KEY (zbor_id)    REFERENCES zboruri(id_zbor)
);
