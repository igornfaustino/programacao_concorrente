package semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Threds em um laço executam uma série de passos e sincronizam em uma barreira
 * a cada passo.Faça um código que implemente uma barreira reusável que feche a
 * si própria após todas as threads passarem
 */

public class ReusableBarrier {
    Semaphore release = new Semaphore(0);
    Semaphore mutex = new Semaphore(1);
    int threadsThatArrival = 0;

    public void waitEveryOne(int n) {
        try {
            mutex.acquire();
            threadsThatArrival++;
            mutex.release();

            if (threadsThatArrival >= n) {
                // ultima thread a chegar
                release.release();
                // libera as outras
            }

            // espera ser liberado
            release.acquire();
            // se passou.. todas chegaram... libera o proximo cara
            mutex.acquire();
            threadsThatArrival--;
            mutex.release();
            if (threadsThatArrival != 0) {
                // se nao saiu todo mundo
                release.release();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static void main(String[] args) {
        new ReusableBarrierThread().start();
        new ReusableBarrierThread().start();
        new ReusableBarrierThread().start();
    }
}

class ReusableBarrierThread extends Thread {
    static ReusableBarrier barrier = new ReusableBarrier();

    @Override
    public void run() {
        try {
            for (int i = 0; i < 4; i++) {
                Thread.sleep(new Random().nextInt(2000));
                System.out.println("Thread esperando");
                barrier.waitEveryOne(3);
                System.out.println("Thread liberada");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}