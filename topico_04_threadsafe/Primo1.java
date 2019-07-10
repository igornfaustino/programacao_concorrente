package topico_04_threadsafe;

/**
 * EXERCICIO 1 SLIDE 18
 * 
 * Fa ̧ca um programa em Java que use Threads para encontraros n ́umeros primos dentro de um intervalo.
 * O metodo quecontabiliza os n ́umeros primos deve possuir como entrada:
 * valor inicial e final do intervalo, n ́umero de threads.
 */

import java.util.List;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.Collections;
import java.util.ArrayList;

public class Primo1 extends Thread {
    public List<Integer> getPrimos(int start, int end, int numThreds) {
        List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
        Thread[] threads = new Thread[numThreds];

        GetNext counter = new GetNext(start, end);

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new CheckPrimoThread(list, counter);
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
        List<Integer> list = new Primo1().getPrimos(0, 100, 5);
        System.out.println(list);
    }
}

class CheckPrimoThread extends Thread {
    List<Integer> primos;
    GetNext counter;

    CheckPrimoThread(List<Integer> primos, GetNext counter) {
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

class GetNext {
    int count;
    int end;

    public GetNext(int start, int end) {
        this.count = start;
        this.end = end;
    }

    public int getNext() throws Exception {
        int value = count++;
        // System.out.println(value);
        if (value > 2 && value % 2 == 0) {
            value = count++;
        }
        if (count > end) {
            throw new ValueException("Bad.");
        }
        return value;
    }
}