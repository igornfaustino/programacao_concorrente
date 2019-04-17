package threadsignal;

/**
 * Implemente o problema do produtor-consumidor que h ́a umbuffer compartilhado
 * entre threads. H ́a uma ́unica threadprodutora e uma ́unica consumidora. O
 * buffer ́e preenchidoem tempos aleat ́orios pela thread produtora. Assim que
 * forproduzido algo, a thread consumidora deve ser comunicadapara obter o
 * valor.
 */

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer extends Thread {
    AtomicInteger buffer;
    WaitNotify monitor;

    public Consumer(AtomicInteger buffer, WaitNotify monitor) {
        this.buffer = buffer;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while(true) {
            monitor.doWait();
            System.out.println(buffer.get());
            buffer.set(-1);
        }
    }
}