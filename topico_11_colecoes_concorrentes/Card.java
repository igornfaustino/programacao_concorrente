package topico_11_colecoes_concorrentes;

/**
 * EXERCICIO 2 slide 9
 * 
 * Implemente duas vers ̃oes do problema do produtor/consumidor com Mprodutores e N consumidores usandoArrayBlockingQueueeLinkedBlockingQueue.  Compare o desempenho das duasimplementa ̧c ̃oes
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

public class Card implements Comparable<Card> {
	int value;
	String naipe;
	String figure;
	static String[] figures = new String[] { "Jack", "Queen", "King" };
	static String[] naipes = new String[] { "Spades", "Heart", "Clubs", "Diamonds" };

	public Card(int value, String naipe, String figure) {
		this.value = value;
		this.naipe = naipe;
		this.figure = figure;
	}

	static int getValue(Card c) {
		if (c.figure.equals("King")) {
			return 13;
		} else if (c.figure.equals("Queen")) {
			return 12;
		} else if (c.figure.equals("Jack")) {
			return 11;
		} else {
			return c.value;
		}
	}

	static Card genereteCard() {
		int value = new Random().nextInt(13) + 1;
		int naipe = new Random().nextInt(4);

		if (value > 10) {
			return new Card(value, naipes[naipe], figures[(value-1) % 10]);
		} else {
			return new Card(value, naipes[naipe], "");
		}
	}

	@Override
	public String toString() {
		if (this.value > 10) {
			return this.figure + " " + this.naipe;
		}
		return Integer.toString(this.value) + " " + this.naipe;
	}

	@Override
	public int compareTo(Card o) {
		if (Card.getValue(this) > Card.getValue(o)) {
			return -1;
		} else if (Card.getValue(this) < Card.getValue(o)) {
			return 1;
		} else {
			return 0;
		}
	}

	public static void main(String[] args) {
		PriorityBlockingQueue<Card> queue = new PriorityBlockingQueue<Card>(10);
		ArrayList<Thread> threads = new ArrayList<Thread>();
		CyclicBarrier consumers = new CyclicBarrier(4);
		CyclicBarrier producers = new CyclicBarrier(4);

		for (int i = 0; i < 2; i++) {
			threads.add(new ProducerCard(queue, consumers, producers));
			threads.get(threads.size() - 1).start();
		}
		for (int i = 0; i < 2; i++) {
			threads.add(new ConsumerCard(queue, producers, consumers));
			threads.get(threads.size() - 1).start();
		}
	}

}

class ProducerCard extends Thread {
	PriorityBlockingQueue<Card> cards;
	CyclicBarrier consumBarrier;
	static Semaphore semaphore = new Semaphore(1);
	CyclicBarrier producBarrier;

	public ProducerCard(PriorityBlockingQueue<Card> cards, CyclicBarrier consumBarrier, CyclicBarrier producBarrier) {
		this.cards = cards;
		this.consumBarrier = consumBarrier;
		this.producBarrier = producBarrier;
	}

	@Override
	public void run() {
		try {
			while (true) {
				// usado para garantir que não estore
				Thread.sleep(500);
				semaphore.acquire();
				if (cards.size() < 10) {
					Card c = Card.genereteCard();
					cards.add(c);
					System.out.println("Produzido: " + c);
					semaphore.release();
				} else {
					semaphore.release();
					System.out.println("produtor esta esperando consumidores");
					// Os dois produtores são travados aqui até os 2 consumidores terminarem
					producBarrier.await();
					consumBarrier.await();
					System.out.println("produtor voltou a produzir");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ConsumerCard extends Thread {
	PriorityBlockingQueue<Card> cards;
	int qtdCards = 0;
	CyclicBarrier producBarrier;
	CyclicBarrier consumBarrier;	

	public ConsumerCard(PriorityBlockingQueue<Card> cards, CyclicBarrier producBarrier, CyclicBarrier consumBarrier) {
		this.cards = cards;
		this.producBarrier = producBarrier;
		this.consumBarrier = consumBarrier;
	}

	@Override
	public void run() {
		try {
			while(true) {
				if (qtdCards == 0) {
					// Espera todos os produtores terminarem
					producBarrier.await();
				}
				Thread.sleep(500);
				System.out.println(cards.take());
				qtdCards++;
				if (qtdCards == 3) {
					qtdCards = 0;
					System.out.println("Consumidor pegou suas 3 cartas");
					consumBarrier.await();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
