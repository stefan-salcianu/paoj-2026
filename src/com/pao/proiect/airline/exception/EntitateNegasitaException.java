package com.pao.proiect.airline.exception;

public class EntitateNegasitaException extends RuntimeException {

    public EntitateNegasitaException(String tipEntitate, String identificator) {
        super(tipEntitate + " cu identificatorul '" + identificator + "' nu a fost gasit in sistem.");
    }
}
