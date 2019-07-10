package topico_03_progthreads;

/**
 * EXERCICIO 4 SLIDE 17
 * 
 * Fa ̧ca uma Thread que monitora um conjunto de threads eexiba quais threads receberam sinais de interrupcao.
 */

import java.util.ArrayList;

public class MonitoringThreads extends Thread {
    ArrayList<Thread> threads;

    MonitoringThreads(ArrayList threads) {
        this.threads = threads;
    }

    @Override
    public void run() {
        while (threads.size() > 0) {
            for (int i = 0; i < threads.size(); i++) {
                if (threads.get(i).isAlive()) {
                    if (threads.get(i).isInterrupted()) {
                        System.out.println(threads.get(i).getName() + " was interrupted");
                    }
                } else {
                    // threads.remove(i);
                }
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        threads.add(new Thread(new TimeRunnable()));
        threads.add(new Thread(new TimeRunnable()));
        threads.add(new Thread(new TimeRunnable()));

        Thread monitor = new MonitoringThreads(threads);
        monitor.start();

        threads.get(0).start();
        threads.get(1).start();
        threads.get(2).start();

        try {
            threads.get(0).interrupt();
            Thread.sleep(1000);
            threads.get(1).interrupt();
            Thread.sleep(1000);
            threads.get(2).interrupt();
        } catch (Exception e) {
            // TODO: handle exception
        }

        monitor.interrupt();
    }
}

class TimeRunnable implements Runnable {
    @Override
    public void run() {
        try {
            long time = (long) (10000);
            Thread.sleep(time);
            System.out.println(Thread.currentThread().getName() + " is dead after " + time + " ms");
        } catch (InterruptedException e) {
            // System.out.println("thread is interrupted");
        }
    }
}