package com.pao.proiect.airline.exception;

public class ZborIndisponibilException extends Exception {

    public ZborIndisponibilException(String idZbor) {
        super("Zborul cu ID-ul '" + idZbor + "' nu este disponibil pentru rezervare.");
    }

    public ZborIndisponibilException(String idZbor, String motiv) {
        super("Zborul cu ID-ul '" + idZbor + "' nu este disponibil: " + motiv + ".");
    }
}
