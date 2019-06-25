package colecoes_concorrentes;

/**
 * Implemente duas vers ̃oes do problema do produtor/consumidor com Mprodutores e N consumidores usandoArrayBlockingQueueeLinkedBlockingQueue.  Compare o desempenho das duasimplementa ̧c ̃oes
 */

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProdCons {
	public static void main(String[] args) {
		ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		Date date = new Date();
		for (int i = 0; i < 15; i++) {
			threads.add(new Producer(queue));
			threads.get(threads.size() - 1).start();
		}
		for (int i = 0; i < 15; i++) {
			threads.add(new Consumer(queue));
			threads.get(threads.size() - 1).start();
		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		System.out.println("Tempo de execucao: " + (new Date().getTime() - date.getTime()));


		LinkedBlockingQueue<Integer> queueLinked = new LinkedBlockingQueue<Integer>(10);
		ArrayList<Thread> threadsLinked = new ArrayList<Thread>();
		
		date = new Date();
		for (int i = 0; i < 15; i++) {
			threadsLinked.add(new ProducerLinked(queueLinked));
			threadsLinked.get(threadsLinked.size() - 1).start();
		}
		for (int i = 0; i < 15; i++) {
			threadsLinked.add(new ConsumerLinked(queueLinked));
			threadsLinked.get(threadsLinked.size() - 1).start();
		}

		for (Thread t : threadsLinked) {
			try {
				t.join();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		System.out.println("Tempo de execucao: " + (new Date().getTime() - date.getTime()));
	}
}

class Producer extends Thread {
	ArrayBlockingQueue<Integer> queue;

	public Producer(ArrayBlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			queue.put(new Random().nextInt(100));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

class Consumer extends Thread {
	ArrayBlockingQueue<Integer> queue;

	public Consumer(ArrayBlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			System.out.println(queue.take());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

class ProducerLinked extends Thread {
	LinkedBlockingQueue<Integer> queue;

	public ProducerLinked(LinkedBlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			queue.put(new Random().nextInt(100));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

class ConsumerLinked extends Thread {
	LinkedBlockingQueue<Integer> queue;

	public ConsumerLinked(LinkedBlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			System.out.println(queue.take());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}