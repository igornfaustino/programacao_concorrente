package topico_10_tasks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.Future;

/**
 * EXERCICIO 2 SLIDE 27
 * 
 * Fa ̧ca um programa que calcule a soma dos elementos de uma matrizMxN. Divida o programa em tarefas que somam as linhas.  O programadeve possibilitar especificar o n ́umero de threads para resolver o problema.
 */

public class SumMatrix {
	public static void main(String[] args) {
		int nThreads = 4;
		int[][] matrix = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);

		Set<Callable<Integer>> callables = new HashSet<Callable<Integer>>();
		for (int i = 0; i < matrix.length; i++) {
			callables.add(new SumLine(matrix[i]));
		}

		try {
			List<Future<Integer>> futures = executor.invokeAll(callables);

			int sum = 0;
			for (Future<Integer> f: futures) {
				sum += f.get();
			}
			System.out.println(sum);
		} catch (Exception e) {

		}
		executor.shutdown();
	}
}

class SumLine implements Callable<Integer> {
	int[] line;

	public SumLine(int[] line) {
		this.line = line;
	}

	@Override
	public Integer call() throws Exception {
		int sum = 0;
		for(int i = 0; i < line.length; i++)
			sum += line[i];
		return sum;
	}
}