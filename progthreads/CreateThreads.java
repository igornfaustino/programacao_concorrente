/**
 * Faca um programa que receba um valor indicando um numero de threads a serem
 * instanciadas e teste os dois modos de criar threads em Java.
 * 
 * * dica:  use oThread.sleeppara pausar as threads por umintervalo de tempo.
 */

package progthreads;

public class CreateThreads {
    public static void main(String[] args) {
        int numThreads = Integer.parseInt(args[0]);

        for (int i = 0; i < numThreads; i++) {
            // new MyThread().start();
            new Thread(new MyRunnable()).start();
        }
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(Thread.currentThread().getName() + ":" + i + "segundo");
                Thread.sleep(1000);
            }
            System.out.println(Thread.currentThread().getName() + ": morrii");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(Thread.currentThread().getName() + ":" + i + "segundo");
                Thread.sleep(1000);
            }
            System.out.println(Thread.currentThread().getName() + ": morrii");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
}