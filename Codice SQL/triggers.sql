-- ====================================================
-- File: triggers.sql
-- Descrizione: Trigger per aggiornare posti_disponibili
-- Autore: Salvatore De Pasquale
-- ====================================================

CREATE OR REPLACE FUNCTION aggiorna_posti_disponibili()
RETURNS TRIGGER AS $$
DECLARE
    -- Variabile per memorizzare i posti totali della sessione
    v_posti_totali INT;
BEGIN

    -- Caso 1: Aggiornamento da (FALSE a TRUE) -> L'utente conferma
    -- --> DECREMENTA posti disponibili
    IF (TG_OP = 'UPDATE' AND OLD.confermata = FALSE AND NEW.confermata = TRUE) THEN
        
        UPDATE SessioneInPresenza
        SET posti_disponibili = posti_disponibili - 1
        WHERE cod_fisc_chef = NEW.cod_fisc_chef
          AND titolo_corso = NEW.titolo_corso
          AND titolo_sessione = NEW.titolo_sessione
          AND posti_disponibili > 0; -- Sicurezza: non scendere sotto 0

    -- Caso 2: Aggiornamento da (TRUE a FALSE) -> L'utente annulla la conferma
    -- --> INCREMENTA posti disponibili
    ELSIF (TG_OP = 'UPDATE' AND OLD.confermata = TRUE AND NEW.confermata = FALSE) THEN
        
        -- Recupero i posti totali per il controllo di sicurezza
        SELECT posti_totali INTO v_posti_totali
        FROM SessioneInPresenza
        WHERE cod_fisc_chef = OLD.cod_fisc_chef
          AND titolo_corso = OLD.titolo_corso
          AND titolo_sessione = OLD.titolo_sessione;
        
        UPDATE SessioneInPresenza
        SET posti_disponibili = posti_disponibili + 1
        WHERE cod_fisc_chef = OLD.cod_fisc_chef
          AND titolo_corso = OLD.titolo_corso
          AND titolo_sessione = OLD.titolo_sessione
          AND posti_disponibili < v_posti_totali; -- Sicurezza: non superare i posti totali
              
    END IF;
    
    
    RETURN NEW;
    
END;
$$ LANGUAGE plpgsql;

-- Trigger su inserimento e aggiornamento di 'confermata'
CREATE TRIGGER trigger_aggiorna_posti
AFTER INSERT OR UPDATE OF confermata ON Adesione
FOR EACH ROW
EXECUTE FUNCTION aggiorna_posti_disponibili();