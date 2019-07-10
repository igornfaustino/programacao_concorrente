package topico_07_semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * EXERCICIO 1 SLIDE 9
 * 
 * Fa ̧ca a implementa ̧c ̃ao do problema do produtor-consumidorusando Sem ́aforos.
 */

public class ProducerConsumer {
    public static void main(String[] args) {
        Semaphore producerSemaphore = new Semaphore(1);
        Semaphore consumerSemaphore = new Semaphore(0);
        Buffer buffer = new Buffer();

        new Producer(buffer, producerSemaphore, consumerSemaphore).start();
        new Consumer(buffer, producerSemaphore, consumerSemaphore).start();
    }
}

class Producer extends Thread {
    Semaphore producerSemaphore;
    Semaphore consumerSemaphore;
    Buffer buffer;

    public Producer(Buffer buffer, Semaphore producerSemaphore, Semaphore consumerSemaphore) {
        this.consumerSemaphore = consumerSemaphore;
        this.producerSemaphore = producerSemaphore;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                producerSemaphore.acquire();
                Thread.sleep(new Random().nextInt(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.buffer.setBuff(new Random().nextInt(100));

            consumerSemaphore.release();
        }
    }
}

class Consumer extends Thread {
    Semaphore producerSemaphore;
    Semaphore consumerSemaphore;
    Buffer buffer;

    public Consumer(Buffer buffer, Semaphore producerSemaphore, Semaphore consumerSemaphore) {
        this.consumerSemaphore = consumerSemaphore;
        this.producerSemaphore = producerSemaphore;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                consumerSemaphore.acquire();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(this.buffer.getBuff());

            producerSemaphore.release();
        }
    }
}

class Buffer {
    int buff = 0;

    /**
     * @param buff the buff to set
     */
    public void setBuff(int buff) {
        this.buff = buff;
    }

    /**
     * @return the buff
     */
    public int getBuff() {
        return buff;
    }
}