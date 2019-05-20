package problemsclassics;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ReaderWriter {
    public static void main(String[] args) {
        Buffer buff = new Buffer();
        ReaderWriterAbstract readerWriter = new ReaderWriterFair();
        new Writer(buff, readerWriter).start();
        new Reader(buff, readerWriter).start();
        new Reader(buff, readerWriter).start();
        // new Reader(buff, readerPriority).start();
        // new Reader(buff, readerPriority).start();
    }
}

abstract class ReaderWriterAbstract {
    int numReaders = 0;
    Semaphore mutex = new Semaphore(1);
    Semaphore wlock = new Semaphore(1);

    // bloqueia escrita e incrementa numReaders
    public abstract void startRead() throws InterruptedException;

    public abstract void EndRead() throws InterruptedException;

    public abstract void startWrite() throws InterruptedException;

    public abstract void endWrite() throws InterruptedException;
}

class ReaderWriterFair extends ReaderWriterAbstract {
    int numReaders = 0;
    Semaphore mutex = new Semaphore(1);
    Semaphore wlock = new Semaphore(1);
    Semaphore queueSemaphore = new Semaphore(1, true);

    // bloqueia escrita e incrementa numReaders
    public void startRead() throws InterruptedException {
        // enter on queue
        queueSemaphore.acquire();
        // regiao critica, check numReaders and block write
        mutex.acquire();
        // se ninguem lendo
        if (numReaders == 0) {
            // espera escrita acabar e bloqueia ela
            wlock.acquire();
        }
        // fala que está lendo
        numReaders++;
        mutex.release();
        queueSemaphore.release();
    }

    public void EndRead() throws InterruptedException {
        mutex.acquire();
        // fala que nao esta lendo
        numReaders--;
        // se ninguem mais esta lendo
        if (numReaders == 0) {
            // libera a escrita
            wlock.release();
        }
        mutex.release();
    }

    public void startWrite() throws InterruptedException {
        // espera pra escrevar
        queueSemaphore.acquire();
        wlock.acquire();
        queueSemaphore.release();
    }

    public void endWrite() throws InterruptedException {
        // libera a escrita
        wlock.release();
    }
}

class ReaderWriterWithReaderPriority extends ReaderWriterAbstract {
    int numReaders = 0;
    Semaphore mutex = new Semaphore(1);
    Semaphore wlock = new Semaphore(1);

    // bloqueia escrita e incrementa numReaders
    public void startRead() throws InterruptedException {
        // regiao critica, check numReaders and block write
        mutex.acquire();
        // se ninguem lendo
        if (numReaders == 0) {
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
        if (numReaders == 0) {
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

class ReaderWriterWithWriterPriority extends ReaderWriterAbstract {
    int numReaders = 0;
    int wantToWrite = 0;
    Semaphore mutex = new Semaphore(1);
    Semaphore wlock = new Semaphore(1);
    Semaphore innerWlock = new Semaphore(1);

    // bloqueia escrita e incrementa numReaders
    public void startRead() throws InterruptedException {
        // regiao critica, check numReaders and block write
        mutex.acquire();
        // fala que está lendo
        numReaders++;
        // se ninguem lendo
        if (numReaders == 1) {
            // libera mutex para o escritor
            mutex.release();
            // espera escrita acabar e bloqueia ela
            wlock.acquire();
        } else {
            mutex.release();
        }
    }

    public void EndRead() throws InterruptedException {
        mutex.acquire();
        // fala que nao esta lendo
        numReaders--;
        // se ninguem mais esta lendo
        if (numReaders == 0) {
            // libera a escrita
            wlock.release();
        }
        mutex.release();
    }

    public void startWrite() throws InterruptedException {
        // espera pra escrevar
        mutex.acquire();
        // update number of writters wating...
        wantToWrite++;
        // if you are the first.. wait until nobody is reading
        if (wantToWrite == 1) {
            // libera o mutex
            mutex.release();
            // espera a sua vez
            wlock.acquire();
        } else {
            mutex.release();
        }
        // wait your turn to write
        innerWlock.acquire();
    }

    public void endWrite() throws InterruptedException {
        // libera a escrita
        mutex.acquire();
        // update number of writters wating...
        wantToWrite--;
        // if you are the last.. release to somebody read
        if (wantToWrite == 0)
            wlock.release();
        mutex.release();
        innerWlock.release();
    }
}

class Reader extends Thread {
    Buffer buff;
    ReaderWriterAbstract readerWriter;

    public Reader(Buffer buff, ReaderWriterAbstract readerWriter) {
        this.buff = buff;
        this.readerWriter = readerWriter;
    }

    @Override
    public void run() {
        while (true) {
            try {
                readerWriter.startRead();
                Thread.sleep(new Random().nextInt(5000));
                System.out.println(Thread.currentThread().getName() + " reads " + buff.getBuff());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    readerWriter.EndRead();
                    Thread.sleep(new Random().nextInt(5000));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        }
    }
}

class Writer extends Thread {
    Buffer buff;
    ReaderWriterAbstract readerWriter;

    public Writer(Buffer buff, ReaderWriterAbstract readerWriter) {
        this.buff = buff;
        this.readerWriter = readerWriter;
    }

    @Override
    public void run() {
        while (true) {
            try {
                readerWriter.startWrite();
                System.out.println("writting...");
                buff.increment();
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                try {
                    readerWriter.endWrite();
                } catch (Exception e) {
                    // TODO: handle exception
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