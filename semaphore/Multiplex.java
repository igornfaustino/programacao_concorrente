package semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Garantir acesso à seção crítica para no máximo N threads.Faça um código que
 * possibilite que N threads estejam na seção crítica simultaneamente.
 */

public class Multiplex {
    public static void main(String[] args) {
        new MultiplexThread().start();
        new MultiplexThread().start();
        new MultiplexThread().start();
        new MultiplexThread().start();
        new MultiplexThread().start();
        new MultiplexThread().start();
        new MultiplexThread().start();
    }
}

class MultiplexThread extends Thread {
    static Semaphore semaphore = new Semaphore(4);
    static AtomicInteger numberOfThreadRunning = new AtomicInteger(0);


    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(numberOfThreadRunning.incrementAndGet() + " threads executando");
            Thread.sleep(new Random().nextInt(3000));
        } catch (Exception e) {
            //TODO: handle exception
        }
        numberOfThreadRunning.decrementAndGet();
        semaphore.release();
        System.out.println("thread saiu da zona critica");
    }
}