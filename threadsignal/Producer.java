package threadsignal;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implemente o problema do produtor-consumidor que h ́a umbuffer compartilhado
 * entre threads. H ́a uma ́unica threadprodutora e uma ́unica consumidora. O
 * buffer ́e preenchidoem tempos aleat ́orios pela thread produtora. Assim que
 * forproduzido algo, a thread consumidora deve ser comunicadapara obter o
 * valor.
 */

public class Producer extends Thread {
    AtomicInteger buffer;
    WaitNotify monitor;
    Random gerador = new Random();

    public Producer(AtomicInteger buffer, WaitNotify monitor) {
        this.buffer = buffer;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(gerador.nextInt(1000));
                buffer.compareAndSet(-1, gerador.nextInt(100));
                monitor.doNotify();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }
}