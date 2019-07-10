package topico_09_locks;

/**
 * EXERCICIO 2 SLIDE 16
 * 
 * Crie uma classe SharedFifoQueue e use Conditions paracontrolar se a fila est ́a vazia ou cheia.  Teste usando threadsprodutoras e consumidoras.
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedFifoQueue {
	int maxSize = 10;
	Queue<Integer> queue = new LinkedList<Integer>();
	Lock lock = new ReentrantLock();
	Condition isFull = lock.newCondition();
	Condition isEmpty = lock.newCondition();

	public void write() {
		try {
			lock.lock();
			while (queue.size() >= maxSize)
				isFull.await();
			queue.add(new Random().nextInt(10));
			isEmpty.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public int take() {
		int element = 0;
		try {
			lock.lock();
			while (queue.size() <= 0)
				isEmpty.await();
			element = queue.poll();
			isFull.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return element;
	}

	public static void main(String[] args) {
		SharedFifoQueue queue = new SharedFifoQueue();

		new Producer(queue).start();
		new Producer(queue).start();
		new Consumer(queue).start();
	}
}

class Producer extends Thread {
	SharedFifoQueue queue;

	public Producer(SharedFifoQueue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(200);
				queue.write();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}

class Consumer extends Thread {
	SharedFifoQueue queue;

	public Consumer(SharedFifoQueue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(200);
				System.out.println(queue.take());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}