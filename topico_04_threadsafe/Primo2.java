package topico_04_threadsafe;

/**
 * EXERCICIO 2 SLIDE 18
 * 
 * Fa ̧ca um programa em Java que use Threads para encontraros n ́umeros primos dentro de um intervalo.
 * O metodo quecontabiliza os n ́umeros primos deve possuir como entrada:
 * valor inicial e final do intervalo, n ́umero de threads.
 */

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.Collections;
import java.util.ArrayList;

public class Primo2 extends Thread {
    public List<Integer> getPrimos(int numThreds, Counter counter) {
        List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
        Thread[] threads = new Thread[numThreds];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new CheckPrimoThreadSafe(list, counter);
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return list;
    }

    public static void main(String[] args) {
        List<Integer> list = new Primo2().getPrimos(5, new CounterMethodLock(0, 100));
        System.out.println(list);
    }
}

class CheckPrimoThreadSafe extends Thread {
    List<Integer> primos;
    Counter counter;

    CheckPrimoThreadSafe(List<Integer> primos, Counter counter) {
        this.counter = counter;
        this.primos = primos;
    }

    void checkPrimo(int value) {
        for (int i = 2; i < value; i++) {
            if (value % i == 0) {
                return;
            }
        }
        primos.add(value);
    }

    @Override
    public void run() {
        while (true) {
            int value;
            try {
                value = counter.getNext();
            } catch (Exception e) {
                break;
            }
            checkPrimo(value);
        }
    }
}

interface Counter {
    public int getNext() throws Exception;
}

class CounterAtomic implements Counter {
    int end;
    AtomicInteger counter;

    CounterAtomic(int start, int end) {
        counter = new AtomicInteger(start);
        this.end = end;
    }

    @Override
    public int getNext() throws Exception {
        int local = counter.getAndIncrement();
        if (local > end) {
            throw new ValueException("Bad.");
        }
        return local;
    }
}

class CounterBlockLock implements Counter {
    int end;
    int counter;

    CounterBlockLock(int start, int end) {
        counter = start;
        this.end = end;
    }

    @Override
    public int getNext() throws Exception {
        int local;
        synchronized (this) {
            local = counter++;
        }
        if (local > end) {
            throw new ValueException("Bad.");
        }
        return local;
    }
}

class CounterMethodLock implements Counter {
    int end;
    int counter;

    CounterMethodLock(int start, int end) {
        counter = start;
        this.end = end;
    }

    @Override
    public synchronized int getNext() throws Exception {
        int local;
        local = counter++;
        if (local > end) {
            throw new ValueException("Bad.");
        }
        return local;
    }
}