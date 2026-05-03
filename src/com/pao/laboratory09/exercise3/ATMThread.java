package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;
import com.pao.laboratory09.exercise1.TipTranzactie;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 4; i++) {
            int id = (atmId - 1) * 4 + i;
            double suma = atmId * 100.0 + i * 50.0;
            Tranzactie t = new Tranzactie(id, suma, "2024-05-03", "", "", TipTranzactie.CREDIT);
            System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON%n", atmId, id, suma);
            try {
                coada.adauga(t, "[ATM-" + atmId + "] astept loc...");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
