package semaphore;

/**
 * Enviar sinal para outra thread para indicar que um evento ocorreu.Faça um
 * código que uma thread t1 e t2 são inicializadas simultaneamente, mas a t2
 * pode somente continuar a execução após a sinalização de t1.
 */

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Signal {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(0);

        new SignalT1(semaphore).start();
        new SignalT2(semaphore).start();
    }
}

class SignalT1 extends Thread {
    Semaphore semaphore;

    public SignalT1(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            // do some process
            System.out.println("t1 executando....");
            Thread.sleep(new Random().nextInt(2000));
            // let T1 execute
            System.out.println("acordando t2");
            semaphore.release();
        } catch (Exception e) {
        }
    }
}

class SignalT2 extends Thread {
    Semaphore semaphore;

    public SignalT2(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            // do some process
            System.out.println("t2 executando....");
        } catch (Exception e) {
        }
    }
}