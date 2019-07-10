package topico_07_semaphore;

/**
 * EXERCICIO 2 DA Atividades: Padrões básicos de sincronização com semáforos
 * 
 * Enviar sinalização para um ponto de encontro entre threads.Faça um código que uma thread t1 e t2 são inicializadas e t1 espera por t2 e vice-versa.Exemplo:t1:trecho1.1trecho1.2t2:trecho2.1trecho2.2thecho1.1 ocorre antes trecho2.2 e threcho2.1 ocorre antes de trecho1.2.
 */

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Rendezvous {
    public static void main(String[] args) {
        Semaphore semaphore1 = new Semaphore(0);
        Semaphore semaphore2 = new Semaphore(0);

        new RendezvousT1(semaphore1, semaphore2).start();
        new RendezvousT2(semaphore1, semaphore2).start();
    }
}

class RendezvousT1 extends Thread {
    Semaphore semaphoret1;
    Semaphore semaphoret2;

    public RendezvousT1(Semaphore semaphoret1, Semaphore semaphoret2) {
        this.semaphoret1 = semaphoret1;
        this.semaphoret2 = semaphoret2;
    }

    @Override
    public void run() {
        try {
            // do some process
            System.out.println("t1 executando parte 1.1....");
            Thread.sleep(new Random().nextInt(2000));
            // let T1 execute
            semaphoret2.release();
            semaphoret1.acquire();
            System.out.println("t1 executando parte 1.2....");
        } catch (Exception e) {
        }
    }
}

class RendezvousT2 extends Thread {
    Semaphore semaphoret1;
    Semaphore semaphoret2;

    public RendezvousT2(Semaphore semaphoret1, Semaphore semaphoret2) {
        this.semaphoret1 = semaphoret1;
        this.semaphoret2 = semaphoret2;
    }

    @Override
    public void run() {
        try {
            System.out.println("t2 executando parte 2.1....");
            Thread.sleep(new Random().nextInt(3000));
            semaphoret1.release();
            semaphoret2.acquire();
            // do some process
            System.out.println("t2 executando parte 2.2....");
        } catch (Exception e) {
        }
    }
}