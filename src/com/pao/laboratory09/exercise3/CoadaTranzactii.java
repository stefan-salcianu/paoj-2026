package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final Queue<Tranzactie> queue = new LinkedList<>();
    private final int capacity;
    private boolean stopped = false;

    public CoadaTranzactii(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void adauga(Tranzactie t, String waitMessage) throws InterruptedException {
        while (queue.size() >= capacity) {
            System.out.println(waitMessage);
            wait();
        }
        queue.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (queue.isEmpty() && !stopped) {
            wait();
        }
        if (queue.isEmpty()) return null;
        Tranzactie t = queue.poll();
        notifyAll();
        return t;
    }

    public synchronized void stop() {
        stopped = true;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
