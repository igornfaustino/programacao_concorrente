package topico_06_threadsignal;

/**
 * Implemente o problema do produtor-consumidor que h ́a umbuffer compartilhado
 * entre threads. H ́a uma ́unica threadprodutora e uma ́unica consumidora. O
 * buffer ́e preenchidoem tempos aleat ́orios pela thread produtora. Assim que
 * forproduzido algo, a thread consumidora deve ser comunicadapara obter o
 * valor.
 */

public class WaitNotify {
    Object myMonitorObject = new Object();
    boolean wasSignalled = false;

    public void doWait() {
        synchronized (myMonitorObject) {
            while (!wasSignalled) {
                try {
                    myMonitorObject.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            wasSignalled = false;
        }
    }

    public void doNotify() {
        synchronized (myMonitorObject) {
            wasSignalled = true;
            myMonitorObject.notify();
        }
    }
}