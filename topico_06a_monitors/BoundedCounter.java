package topico_06a_monitors;

import java.util.Random;

/**
 * EXERCICIO 3 SLIDE 12
 * 
 * Escreva um monitor BoundedCounter que possui um valorm ́ınimo e m ́aximo. A
 * classe possui dois m ́etodos:increment()edecrement(). Ao alcan ̧car os
 * limites m ́ınimo ou m ́aximo, athread que alcan ̧cou deve ser bloqueada.
 */

public class BoundedCounter {
    int count;
    int maxValue;
    int minValue;

    public BoundedCounter(int value, int maxValue, int minValue) {
        count = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public synchronized void increment() {
        if (count < maxValue) {
            count++;
        }

        boolean isBlocked = (count == maxValue);
        while(isBlocked) {
            try {
                wait();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

    public synchronized void decrement() {
        if (count > minValue) {
            count--;
        }

        boolean isBlocked = (count == minValue);
        while(isBlocked) {
            try {
                wait();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

    public synchronized int get() {
        return count;
    }

    public static void main(String[] args) {
        new Thread() {
            BoundedCounter monitor = new BoundedCounter(0, 10, 0);

            @Override
            public void run() {
                while(true) {
                    monitor.increment();
                    System.out.println(monitor.get());
                }
            }
        }.start();
    }
}