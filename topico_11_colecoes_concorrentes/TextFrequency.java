package topico_11_colecoes_concorrentes;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;

/**
 * EXERCICIO 1 SLIDE 17
 * 
 * Fa ̧ca um programa usandoThreadseConcurrentMapparacalcular a frequˆencia de cada letra em um texto.
 */

public class TextFrequency implements Callable<Void> {
	char[] text;
	int startPos;
	int endPos;
	ConcurrentHashMap<Character, Integer> frequency;

	public TextFrequency(char[] text, int startPos, int endPos, ConcurrentHashMap<Character, Integer> frequency) {
		this.text = text;
		this.startPos = startPos;
		this.endPos = endPos;
		this.frequency = frequency;
	}

	@Override
	public Void call() throws Exception {
		for (int i = startPos; i <= endPos; i++) {
			frequency.computeIfPresent(text[i], (k, v) -> v + 1);
		}
		return null;
	}

	public static void main(String[] args) {
		char[] text = "Lorem Ipsum is simply dummy text of the printing and".replaceAll("\\W", "").toLowerCase()
				.toCharArray();

		ConcurrentHashMap<Character, Integer> frequency = new ConcurrentHashMap<Character, Integer>();
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		for (char letter : alphabet) {
			frequency.putIfAbsent(letter, 0);
		}

		ExecutorService eService = Executors.newFixedThreadPool(4);

		System.out.println("len: " + text.length);
		int nTasks = 4;
		int qtdPerTask = text.length / 4;
		int startPos = 0;

		Set<Callable<Void>> tasks = new HashSet<Callable<Void>>();
		for (int i = 0; i < nTasks - 1; i++) {
			tasks.add(new TextFrequency(text, startPos, startPos + qtdPerTask - 1, frequency));
			startPos += qtdPerTask;
		}
		tasks.add(new TextFrequency(text, startPos, text.length - 1, frequency));

		try {
			List<Future<Void>> results = eService.invokeAll(tasks);
			for (Future<Void> f : results) {
				f.get();
			}
			frequency.forEach((k, v) -> System.out.println(k + ": " + v));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eService.shutdown();
		}
	}
}