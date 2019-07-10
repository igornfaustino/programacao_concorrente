package topico_06a_monitors;

import java.util.Random;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

/**
 * EXERCICIO 1 SLIDE 12
 * 
 * Implemente uma solu ̧c ̃ao com monitor para o problema doProdutor-Consumidor
 * usando um buffer circular.
 */

public class CircularBufferMonitor {
    CircularBuffer buffer;

    public CircularBufferMonitor(int size) {
        buffer = new CircularBuffer(size);
    }

    public synchronized void put(int val) {
        while (buffer.isFull()) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        buffer.insert(val);
        notifyAll();
    }

    public synchronized int get() {
        while (buffer.isEmpty()) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        notifyAll();
        return buffer.getValue();
    }

    public static void main(String[] args) {
        CircularBufferMonitor monitor = new CircularBufferMonitor(10);
        new Producer(monitor).start();
        new Producer(monitor).start();
        new Consumer(monitor).start();
    }
}

class CircularBuffer {
    private int[] buffer;
    private int insertPos;
    private int getPos;
    private boolean empty;
    private boolean full;

    public CircularBuffer(int size) {
        buffer = new int[size];
        insertPos = 0;
        getPos = 0;
        empty = true;
        full = false;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void insert(int val) {
        if (full) {
            throw new ValueException("buffer is full");
        }

        // if buffer is not full... insertion will make the buffer not empty
        empty = false;
        buffer[insertPos] = val;

        // update insertPos
        insertPos = (insertPos + 1) % buffer.length;
        if (insertPos == getPos) {
            full = true;
        }
    }

    public int getValue() {
        if (empty) {
            throw new ValueException("buffer is empty");
        }
        full = false;
        int val = buffer[getPos];
        getPos = (getPos + 1) % buffer.length;
        if (getPos == insertPos) {
            empty = true;
        }
        return val;
    }
}

class Producer extends Thread {
    CircularBufferMonitor monitor;
    Random gerador = new Random();

    public Producer(CircularBufferMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(gerador.nextInt(1000));
                monitor.put(gerador.nextInt(100));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}

class Consumer extends Thread {
    CircularBufferMonitor monitor;

    public Consumer(CircularBufferMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(monitor.get());
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}