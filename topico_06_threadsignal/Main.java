package topico_06_threadsignal;

/**
 * EXERCICIO 1 SLIDE 16
 * 
 * Implemente o problema do produtor-consumidor que h ́a umbuffer compartilhado
 * entre threads. H ́a uma ́unica threadprodutora e uma ́unica consumidora. O
 * buffer ́e preenchidoem tempos aleat ́orios pela thread produtora. Assim que
 * forproduzido algo, a thread consumidora deve ser comunicadapara obter o
 * valor.
 */

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        AtomicInteger buffer = new AtomicInteger(-1);
        WaitNotify monitor = new WaitNotify();

        new Consumer(buffer, monitor).start();
        new Producer(buffer, monitor).start();
    }
}