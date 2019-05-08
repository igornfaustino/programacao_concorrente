package problemsclassics;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ReaderWriter {
    int numReaders = 0;
    Semaphore mutex = new Semaphore(1);
    Semaphore wlock = new Semaphore(1);

    // bloqueia escrita e incrementa numReaders
    public void startRead() throws InterruptedException {
        // regiao critica, check numReaders and block write
        mutex.acquire();
        // se ninguem lendo
        if (numReaders == 0){
            // espera escrita acabar e bloqueia ela
            wlock.acquire();
        }
        // fala que está lendo
        numReaders++;
        mutex.release();
    }

    public void EndRead() throws InterruptedException {
        mutex.acquire();
        // fala que nao esta lendo
        numReaders--;
        // se ninguem mais esta lendo
        if (numReaders == 0){
            // libera a escrita
            wlock.release();
        }
        mutex.release();
    }

    public void startWrite() throws InterruptedException {
        // espera pra escrevar
        wlock.acquire();
    }

    public void endWrite() throws InterruptedException {
        // libera a escrita
        wlock.release();
    }
}

class Reader extends Thread {
    Buffer buff;
    ReaderWriter readerWriter;

    public Reader(Buffer buff, ReaderWriter readerWriter) {
        this.buff = buff;
        this.readerWriter = readerWriter;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(new Random().nextInt(5000));
                readerWriter.startRead();
                System.out.println(Thread.currentThread().getName() + " reads " + buff.getBuff());
            } catch (Exception e) {
                //TODO: handle exception
            } finally {
                try {
                    readerWriter.EndRead();
                } catch (Exception e) {
                    //TODO: handle exception
                }
            }

        }
    }
}

class Writer extends Thread {
    Buffer buff;
    ReaderWriter readerWriter;

    public Writer(Buffer buff, ReaderWriter readerWriter) {
        this.buff = buff;
        this.readerWriter = readerWriter;
    }

    @Override
    public void run() {
        while(true){
            try {
                readerWriter.startWrite();
                buff.increment();
            } catch (Exception e) {
                //TODO: handle exception
            } finally {
                try {
                    readerWriter.endWrite();
                } catch (Exception e) {
                    //TODO: handle exception
                }
            }

        }
    }
}

class Buffer {
    int buff = 0;

    /**
     * @return the buff
     */
    public int getBuff() {
        return buff;
    }

    /**
     * @param buff the buff to set
     */
    public void increment() {
        buff++;
    }
}