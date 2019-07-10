package topico_07_semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * EXERCICIO 5 DA Atividades: Padrões básicos de sincronização com semáforos
 * 
 * Após n threads sincronizarem em um ponto em comum, um trecho específico
 * (ponto crítico) pode ser executado por cada uma delas.Faça um código que
 * possibilite barrar N threads em um ponto específico de execução e, após todas
 * alcançarem o ponto, todas devem executar o trecho de código após esse ponto.
 */

public class Barrier {
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
            release.release();
        } catch (Exception e) {
            //TODO: handle exception
        }

    }

    public static void main(String[] args) {
        new BarrierThread().start();
        new BarrierThread().start();
        new BarrierThread().start();
        new BarrierThread().start();
    }
}

class BarrierThread extends Thread {
    static Barrier barrier = new Barrier();

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(2000));
            System.out.println("Thread esperando");
            barrier.waitEveryOne(3);
            System.out.println("Thread liberada");
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}