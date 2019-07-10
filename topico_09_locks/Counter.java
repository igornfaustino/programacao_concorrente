package topico_09_locks;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * EXERCICIO 1 SLIDE 16
 * 
 * Fa ̧ca um programa usando Lock para simular a atualiza ̧c ̃ao deum contador que  ́e acessado por m ́ultiplas threads.  Ocontador pode diminuir e aumentar.
 */

public class Counter {
	int c = 0;
	Lock lock = new ReentrantLock();

	int IncreaseAndGet() {
		int value = 0;
		try {
			lock.lock();
			value = c++;
		} finally {
			lock.unlock();
		}
		return value;
	}

	int DecreaseAndGet() {
		int value = 0;
		try {
			lock.lock();
			value = c--;
		} finally {
			lock.unlock();
		}
		return value;
	}

	public static void main(String[] args) {
		Counter counter = new Counter();

		new CounterThread(counter).start();
		new CounterThread(counter).start();
		new CounterThread(counter).start();
		new CounterThread(counter).start();
	}
}

class CounterThread extends Thread {
	Counter counter;

	public CounterThread(Counter counter) {
		this.counter = counter;
	}

	@Override
	public void run() {
		while(true) {
			if (new Random().nextBoolean()) {
				System.out.println(counter.IncreaseAndGet());
			} else {
				System.out.println(counter.DecreaseAndGet());
			}
		}
	}
}