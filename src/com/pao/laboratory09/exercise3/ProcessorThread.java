package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;
    private final CoadaTranzactii coada;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Tranzactie t = coada.extrage(); // blocks until item or stopped+empty → null
                if (t == null) break;
                System.out.printf("[Processor] Factura #%d - %.2f RON | %s%n",
                        t.getId(), t.getSuma(), t.getData());
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
