package topico_09_locks;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * EXERCICIO 3 SLIDE 16
 * 
 * Fa ̧ca uma classe ArrayListThreadSafe usando ReadWriteLock.Teste usando
 * threads que realizam leitura e escrita para essaestrutura.
 */

public class ArrayListThreadSafe {
	ArrayList<Integer> list = new ArrayList<Integer>();
	ReentrantReadWriteLock rWriteLock = new ReentrantReadWriteLock();
	Lock readLock = rWriteLock.readLock();
	Lock writeLock = rWriteLock.writeLock();

	public void add(int value) {
		writeLock.lock();
		try {
			list.add(value);
		} finally {
			writeLock.unlock();
		}
	}

	public int get(int index) {
		readLock.lock();
		try {
			return list.get(index);
		} finally {
			readLock.unlock();
		}
	}

	public int getSize() {
		readLock.lock();
		try {
			return list.size();
		} finally {
			readLock.unlock();
		}
	}

	public static void main(String[] args) {
		ArrayListThreadSafe list = new ArrayListThreadSafe();

		new ReaderThread(list).start();
		new ReaderThread(list).start();
		new WriterThread(list).start();
	}
}

class ReaderThread extends Thread {
	ArrayListThreadSafe list;

	public ReaderThread(ArrayListThreadSafe list) {
		this.list = list;
	}

	@Override
	public void run() {
		while (true) {
			int idx = list.getSize() - 1;
			if (idx > 0)
				System.out.println(list.get(idx));
		}
	}
}

class WriterThread extends Thread {
	ArrayListThreadSafe list;

	public WriterThread(ArrayListThreadSafe list) {
		this.list = list;
	}

	@Override
	public void run() {
		while (true) {
			list.add(new Random().nextInt(100));
		}
	}
}