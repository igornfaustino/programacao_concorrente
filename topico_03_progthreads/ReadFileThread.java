package topico_03_progthreads;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * EXERCICIO 2 SLIDE 17
 * 
 * Faca uma thread Java que realize a leitura de um arquivotexto com frases e
 * exiba as frases a cada 10 segundos.
 *
 * @author igornfaustino
 */

public class ReadFileThread {
    public static void main(String[] args) {
    
        new Thread(new ReadFileRunnable("progthreads/file.txt")).start();
    }
}

class ReadFileRunnable implements Runnable {
    BufferedReader reader;

    ReadFileRunnable(String filePath) {
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