package progthreads;

/**
 * Fa ̧ca um programa em Java que consulte periodicamente o estado de um conjunto de threads. 
 */

import java.util.ArrayList;

public class StateMonitoring extends Thread {
    ArrayList<Thread> threads;

    StateMonitoring(ArrayList threads) {
        this.threads = threads;
    }

    @Override
    public void run() {
        String[] threadState = new String[threads.size()];
        while (threads.size() > 0) {
            for (int i = 0; i < threads.size(); i++) {
                String state = threads.get(i).getState().name();
                if (threadState[i] == null|| !threadState[i].equals(state)){
                    threadState[i] = state;
                    System.out.println(threads.get(i).getName() + ": " + state);
                }
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        threads.add(new Thread(new TimerRunnable()));
        threads.add(new Thread(new TimerRunnable()));
        threads.add(new Thread(new TimerRunnable()));

        Thread monitor = new StateMonitoring(threads);
        monitor.start();

        threads.get(0).start();
        threads.get(1).start();
        threads.get(2).start();

        try {
            // threads.get(0).interrupt();
            Thread.sleep(1000);
            threads.get(1).interrupt();
            Thread.sleep(1000);
            threads.get(2).interrupt();
            Thread.sleep(1000);
        } catch (Exception e) {
            // TODO: handle exception
        }

        monitor.interrupt();
    }
}

class TimerRunnable implements Runnable {
    @Override
    public void run() {
        try {
            long time = (long) (10000);
            Thread.sleep(time);
            Thread t = new Thread(()->{
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    //TODO: handle exception
                }
            });
            t.start();
            t.join();
            for(int i = 0; i< 100000; i++);
            System.out.println(Thread.currentThread().getName() + " is dead after " + time + " ms");
        } catch (InterruptedException e) {
        }
    }
}