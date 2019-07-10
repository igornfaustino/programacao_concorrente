package topico_06a_monitors;

import java.util.Random;

/**
 * EXERCICIO 2 SLIDE 12
 * 
 * Escreva uma monitor Counter que possibilita um processodormir at ́e o
 * contador alcan ̧car um valor. A classe Counterpermite duas opera ̧c
 * ̃oes:increment()esleepUntil(int x).
 */

public class Counter {
    int count;

    public Counter() {
        count = 0;
    }

    public synchronized void increment() {
        count++;
        notifyAll();
    }

    public synchronized int sleepUntil(int value) {
        while (count < value) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        notifyAll();
        return count;
    }

    public static void main(String[] args) {
        Counter monitor = new Counter();
        new Increment(monitor).start();
        new Until(monitor, 5).start();
        new Until(monitor, 15).start();
        new Until(monitor, 200).start();
    }
}

class Increment extends Thread {
    Counter monitor;

    public Increment(Counter monitor) {
        this.monitor = monitor;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(new Random().nextInt(1000));
                monitor.increment();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}

class Until extends Thread {
    Counter monitor;
    int value;

    public Until(Counter monitor, int value) {
        this.monitor = monitor;
        this.value = value;
    }

    public void run() {
        try {
            int v = monitor.sleepUntil(value);
            System.out.println("acordei no valor " + v);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}