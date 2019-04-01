package progthreads;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Faca um programa Java que envia interrupcoes para as threads dos exercicios anteriores.  
 * As threads devem fazer o tratamento dessas interrupcoees e realizar uma finalizacao limpa.
 */

public class InterruptThread {
    public static void main(String[] args) {
        Thread ex1 = new Thread(new RandomTimeRunnableEx1());
        Thread ex2 = new Thread(new ReadFileRunnableEx2("progthreads/file.txt"));

        ex1.start();
        ex2.start();
        ex1.interrupt();
        ex2.interrupt();
    }
}

class RandomTimeRunnableEx1 implements Runnable {
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

class ReadFileRunnableEx2 implements Runnable {
    BufferedReader reader;

    ReadFileRunnableEx2(String filePath) {
        try {            
            this.reader = new BufferedReader(new FileReader(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String strCurrentLine;
        try {
            while ((strCurrentLine = reader.readLine()) != null) {
                try {
                    System.out.println(strCurrentLine);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}