package topico_07_semaphore;

/**
 * EXERCICIO 7 DA Atividades: Padrões básicos de sincronização com semáforos
 * 
 * Semáforos podem ser usadas para representar uma fila.Faça um código que implemente duas filas (F1 e F2) com semáforos. 
 * As threads colocadas na F1 sópodem executar se tiver um par na F2. Nesse caso, ambas as threads na primeira fila são 
 * executadas.
 */

import java.util.concurrent.Semaphore;

public class Queue {
    Semaphore semaphore = new Semaphore(0, true);
    static Semaphore mutex = new Semaphore(1);
    int numOfThreadsWaitings = 0;

    void waitInQueue() throws Exception {
        semaphore.acquire();
    }

    void releaseFirst() {
        semaphore.release();
    }

    public static void main(String[] args) {
        Queue f1 = new Queue();
        Queue f2 = new Queue();

        try {
            new QueueThread("f1 1", f1, f2).start();
            Thread.sleep(1000);
            new QueueThread("f2 1", f2, f1).start();
            Thread.sleep(1000);
            new QueueThread("f1 2", f1, f2).start();
            Thread.sleep(1000);
            new QueueThread("f1 3", f1, f2).start();
            Thread.sleep(1000);
            new QueueThread("f2 2", f2, f1).start();
            Thread.sleep(1000);
            new QueueThread("f2 3", f2, f1).start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

class QueueThread extends Thread {
    Queue f1;
    Queue f2;
    String name;

    public QueueThread(String name, Queue f1, Queue f2) {
        this.f1 = f1;
        this.f2 = f2;
        this.name = name;
    }

    @Override
    public void run() {

        try {
            System.out.println(name + " esta esperando");
            f2.releaseFirst();
            f1.waitInQueue();
            System.out.println(name + " passou");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}