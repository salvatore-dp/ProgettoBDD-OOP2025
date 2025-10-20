-- ====================================================
    -- File: create_tables.sql
    -- Descrizione: Definisce tutta la struttura della base di dati
    -- Autore: Salvatore De Pasquale
-- ====================================================

BEGIN;

-- Definizione tabella Chef

CREATE TABLE Chef(
    codice_fiscale VARCHAR (16) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL,
    pass_hash VARCHAR(50) NOT NULL
);

-- Definizione tabella Corso

CREATE TABLE Corso(
    cod_fisc_chef VARCHAR(16) NOT NULL REFERENCES Chef(codice_fiscale) ON DELETE CASCADE,
    titolo VARCHAR(50) NOT NULL,
    frequenza VARCHAR(50),
    data_inizio DATE NOT NULL,
    PRIMARY KEY(cod_fisc_chef, titolo)
);

-- Creiamo l'ENUM per la tabella Ricetta

CREATE TYPE stadi_difficolta AS ENUM ('facile', 'medio', 'difficile');

-- Definizione tabella Ricetta

CREATE TABLE Ricetta(
    cod_fisc_chef VARCHAR(16) NOT NULL REFERENCES Chef(codice_fiscale) ON DELETE CASCADE,
    titolo_ricetta VARCHAR(50) NOT NULL,
    descrizione TEXT,
    difficolta stadi_difficolta,
    tempo_prep INT,
    porzioni INT,
    data_creazione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cod_fisc_chef, titolo_ricetta, data_creazione)
);

-- Definizione tabella Utente

CREATE TABLE Utente(
    username VARCHAR(30) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL,
    pass_hash VARCHAR(50) NOT NULL
);

-- Definizione tabella Ingrediente

CREATE TABLE Ingrediente(
    nome_ingrediente VARCHAR(25) PRIMARY KEY,
    descrizione_ingrediente TEXT
);

-- Definizione SessioneOnline

CREATE TABLE SessioneOnline (
    cod_fisc_chef VARCHAR(16) NOT NULL,
    titolo_corso VARCHAR(50) NOT NULL,
    titolo_sessione VARCHAR(50) NOT NULL,
    data_ora TIMESTAMP NOT NULL,
    durata INT,
    link_video VARCHAR(2083),
    PRIMARY KEY (cod_fisc_chef, titolo_corso, titolo_sessione),
    FOREIGN KEY (cod_fisc_chef, titolo_corso) REFERENCES Corso(cod_fisc_chef, titolo) ON DELETE CASCADE
);

-- Definizione SessioneInPresenza

CREATE TABLE SessioneInPresenza(
    cod_fisc_chef VARCHAR(16) NOT NULL,
    titolo_corso VARCHAR(50) NOT NULL,
    titolo_sessione VARCHAR(50) NOT NULL,
    data_ora TIMESTAMP NOT NULL,
    durata INT,
    luogo VARCHAR(100),
    posti_totali INT,
    posti_disponibili INT,
    PRIMARY KEY (cod_fisc_chef, titolo_corso, titolo_sessione),
    FOREIGN KEY (cod_fisc_chef, titolo_corso) REFERENCES Corso(cod_fisc_chef, titolo) ON DELETE CASCADE
);

-- Definizione Iscrizione

CREATE TABLE Iscrizione(
    username_utente VARCHAR(30) NOT NULL,
    cod_fisc_chef VARCHAR(16) NOT NULL,
    titolo_corso VARCHAR(50) NOT NULL,
    data_iscrizione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (username_utente, cod_fisc_chef, titolo_corso),
    FOREIGN KEY (cod_fisc_chef, titolo_corso) REFERENCES Corso(cod_fisc_chef, titolo) ON DELETE CASCADE,
    FOREIGN KEY (username_utente) REFERENCES Utente(username) ON DELETE CASCADE
);

-- Definizione Adesione

CREATE TABLE Adesione(
    username_utente VARCHAR(30) NOT NULL,
    titolo_corso VARCHAR(50) NOT NULL,
    titolo_sessione VARCHAR(50) NOT NULL,
    cod_fisc_chef VARCHAR(16) NOT NULL,
    data_adesione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confermata BOOLEAN,
    PRIMARY KEY (username_utente, cod_fisc_chef, titolo_corso, titolo_sessione),
    FOREIGN KEY (username_utente) REFERENCES Utente(username) ON DELETE CASCADE,
    FOREIGN KEY (cod_fisc_chef, titolo_corso, titolo_sessione) REFERENCES SessioneInPresenza(cod_fisc_chef, titolo_corso, titolo_sessione) ON DELETE CASCADE
);

-- Definizione ProgrammaSessione

CREATE TABLE ProgrammaSessione(
    cod_fisc_chef VARCHAR(16) NOT NULL,
    titolo_ricetta VARCHAR(50) NOT NULL,
    data_creazione_ricetta TIMESTAMP NOT NULL,
    titolo_corso VARCHAR(50) NOT NULL,
    titolo_sessione VARCHAR(50) NOT NULL,
    porzioni_per_partec DOUBLE PRECISION,
    PRIMARY KEY (cod_fisc_chef, titolo_ricetta, data_creazione_ricetta, titolo_corso, titolo_sessione),
    FOREIGN KEY (cod_fisc_chef, titolo_ricetta, data_creazione_ricetta) REFERENCES Ricetta(cod_fisc_chef, titolo_ricetta, data_creazione) ON DELETE CASCADE,
    FOREIGN KEY (cod_fisc_chef, titolo_corso, titolo_sessione) REFERENCES SessioneInPresenza(cod_fisc_chef, titolo_corso, titolo_sessione) ON DELETE CASCADE
);

-- Definizione di ComposizioneRicetta

CREATE TABLE ComposizioneRicetta(
    cod_fisc_chef VARCHAR(16) NOT NULL,
    nome_ingrediente VARCHAR(25) NOT NULL,
    titolo_ricetta VARCHAR(50) NOT NULL,
    data_creazione_ricetta TIMESTAMP NOT NULL,
    quantita_ingrediente DECIMAL(6,2),
    unita_spec_ingrediente VARCHAR(10),
    PRIMARY KEY (cod_fisc_chef, titolo_ricetta, data_creazione_ricetta, nome_ingrediente),
    FOREIGN KEY (cod_fisc_chef, titolo_ricetta, data_creazione_ricetta) REFERENCES Ricetta(cod_fisc_chef, titolo_ricetta, data_creazione) ON DELETE CASCADE,
    FOREIGN KEY (nome_ingrediente) REFERENCES Ingrediente(nome_ingrediente) ON DELETE RESTRICT
);

COMMIT;