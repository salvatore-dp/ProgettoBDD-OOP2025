-- ====================================================
-- File: populate_database.sql
-- Descrizione: Popola il database UninaFoodLab con dati
-- Autore: Salvatore De Pasquale
-- ====================================================

BEGIN;

-- -------------------------
-- TABELLA CHEF
-- -------------------------
INSERT INTO Chef (codice_fiscale, nome, cognome, email, pass_hash)
VALUES 
('RSSMRA85M01H501U', 'Mario', 'Rossi', 'mario.rossi@example.com', 'hashpassword123'),
('BNCLRA90L45F839K', 'Lara', 'Bianchi', 'lara.bianchi@example.com', 'hashpassword456');

-- -------------------------
-- TABELLA CORSO
-- -------------------------
INSERT INTO Corso (cod_fisc_chef, titolo, frequenza, data_inizio)
VALUES 
('RSSMRA85M01H501U', 'Cucina Italiana', 'settimanale', '2025-10-20'),
('BNCLRA90L45F839K', 'Dolci al Cucchiaio', 'mensile', '2025-11-05');

-- -------------------------
-- TABELLA UTENTE
-- -------------------------
INSERT INTO Utente (username, nome, cognome, email, pass_hash)
VALUES
('giulia123', 'Giulia', 'Bianchi', 'giulia.bianchi@example.com', 'hashpass456'),
('andrea88', 'Andrea', 'Verdi', 'andrea.verdi@example.com', 'hashpass789');

-- -------------------------
-- TABELLA INGREDIENTE
-- -------------------------
INSERT INTO Ingrediente (nome_ingrediente, descrizione_ingrediente)
VALUES
('Pomodoro', 'Pomodoro fresco maturo'),
('Basilico', 'Foglie di basilico fresco'),
('Zucchero', 'Zucchero semolato'),
('Latte', 'Latte intero fresco');

-- -------------------------
-- TABELLA RICETTA
-- -------------------------
INSERT INTO Ricetta (cod_fisc_chef, titolo_ricetta, descrizione, difficolta, tempo_prep, porzioni, data_creazione)
VALUES
('RSSMRA85M01H501U', 'Pasta al Pomodoro', 'Ricetta tradizionale italiana', 'facile', 30, 4, '2025-10-15 18:00:00'),
('BNCLRA90L45F839K', 'Panna Cotta', 'Dolce al cucchiaio con panna e zucchero', 'medio', 45, 6, '2025-10-14 10:30:00');

-- -------------------------
-- TABELLA SESSIONE IN PRESENZA
-- -------------------------
INSERT INTO SessioneInPresenza (cod_fisc_chef, titolo_corso, titolo_sessione, data_ora, durata, luogo)
VALUES
('RSSMRA85M01H501U', 'Cucina Italiana', 'Lezione 1', '2025-10-21 15:30:00', 120, 'Napoli'),
('BNCLRA90L45F839K', 'Dolci al Cucchiaio', 'Lezione 1', '2025-11-07 17:00:00', 90, 'Milano');

-- -------------------------
-- TABELLA SESSIONE ONLINE
-- -------------------------
INSERT INTO SessioneOnline (cod_fisc_chef, titolo_corso, titolo_sessione, data_ora, durata, link_video)
VALUES
('RSSMRA85M01H501U', 'Cucina Italiana', 'Lezione Online 1', '2025-10-23 18:00:00', 90, 'https://zoom.example.com/mario'),
('BNCLRA90L45F839K', 'Dolci al Cucchiaio', 'Lezione Online 1', '2025-11-09 20:30:00', 60, 'https://zoom.example.com/lara');

-- -------------------------
-- TABELLA ISCRIZIONE
-- -------------------------
INSERT INTO Iscrizione (username_utente, cod_fisc_chef, titolo_corso, data_iscrizione)
VALUES
('giulia123', 'RSSMRA85M01H501U', 'Cucina Italiana', '2025-10-15 18:10:00'),
('andrea88', 'BNCLRA90L45F839K', 'Dolci al Cucchiaio', '2025-10-16 09:45:00');

-- -------------------------
-- TABELLA ADESIONE
-- -------------------------
INSERT INTO Adesione (username_utente, titolo_corso, titolo_sessione, cod_fisc_chef, data_adesione, confermata)
VALUES
('giulia123', 'Cucina Italiana', 'Lezione 1', 'RSSMRA85M01H501U', '2025-10-17 10:00:00', TRUE),
('andrea88', 'Dolci al Cucchiaio', 'Lezione 1', 'BNCLRA90L45F839K', '2025-10-18 11:00:00', FALSE);

-- -------------------------
-- TABELLA COMPOSIZIONE RICETTA
-- -------------------------
INSERT INTO ComposizioneRicetta (cod_fisc_chef, nome_ingrediente, titolo_ricetta, data_creazione_ricetta, quantita_ingrediente, unita_spec_ingrediente)
VALUES
('RSSMRA85M01H501U', 'Pomodoro', 'Pasta al Pomodoro', '2025-10-15 18:00:00', 200.0, 'g'),
('RSSMRA85M01H501U', 'Basilico', 'Pasta al Pomodoro', '2025-10-15 18:00:00', 5.0, 'foglie'),
('BNCLRA90L45F839K', 'Latte', 'Panna Cotta', '2025-10-14 10:30:00', 500.0, 'ml'),
('BNCLRA90L45F839K', 'Zucchero', 'Panna Cotta', '2025-10-14 10:30:00', 100.0, 'g');

-- -------------------------
-- TABELLA PROGRAMMA SESSIONE
-- -------------------------
INSERT INTO ProgrammaSessione (cod_fisc_chef, titolo_ricetta, data_creazione_ricetta, titolo_corso, titolo_sessione, porzioni_per_partec)
VALUES
('RSSMRA85M01H501U', 'Pasta al Pomodoro', '2025-10-15 18:00:00', 'Cucina Italiana', 'Lezione 1', 2.0),
('BNCLRA90L45F839K', 'Panna Cotta', '2025-10-14 10:30:00', 'Dolci al Cucchiaio', 'Lezione 1', 1.5);

COMMIT;