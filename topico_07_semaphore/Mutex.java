package topico_07_semaphore;

/**
 * EXERCICIO 3 DA Atividades: Padrões básicos de sincronização com semáforos
 * 
 * Garantir acesso exclusivo à seção crítica.Faça um código que possibilite que
 * 2 ou mais threads realizem o incremento de um contador. Faça aexclusão mútua
 * com semáforo
 */

import java.util.concurrent.Semaphore;

public class Mutex {
    public static void main(String[] args) {
        Counter c = new Counter();

        new MutexThread(c).start();
        new MutexThread(c).start();
        new MutexThread(c).start();
        new MutexThread(c).start();
        new MutexThread(c).start();
    }
}

class MutexThread extends Thread {
    Counter counter;
    public MutexThread(Counter c) {
        counter = c;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(counter.incrementAndGet());
        }
    }
}

class Counter {
    Semaphore semaphore = new Semaphore(1);
    int counter = 0;

    public int incrementAndGet() {
        try {
            semaphore.acquire();
        } catch (Exception e) {
            //TODO: handle exception
        }
        counter++;
        semaphore.release();
        return counter;
    }
}