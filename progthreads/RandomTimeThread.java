/**
 * Faca um programa em Java que inicie tres threads e, cada thread, espere um tempo aleatorio para terminar
 */

package progthreads;

public class RandomTimeThread {
    public static void main(String[] args) {
        new Thread(new RandomTimeRunnable()).start();
        new Thread(new RandomTimeRunnable()).start();
        new Thread(new RandomTimeRunnable()).start();
    }
}

class RandomTimeRunnable implements Runnable {
    @Override
    public void run() {
        try {
            long time = (long)(Math.random() * 1000);
            Thread.sleep(time);
            System.out.println(Thread.currentThread().getName() + " is dead after " + time + " ms");
        } catch (InterruptedException e) {
            System.out.println("thread is interrupted");
        }
    }
}