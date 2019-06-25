package tasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.Future;

public class MultiplyMatrix {
	public static void main(String[] args) {
		int nThreads = 4;
		int[][] matrix1 = new int[][] { { 1, 2 }, { 3, 4 } };
		int[][] matrix2 = new int[][] { { -1, 3 }, { 4, 2 } };
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);

		List<Callable<Integer>> callables = new ArrayList<Callable<Integer>>();
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1[i].length; j++) {
				callables.add(new MultiplyValue(matrix1, matrix2, i, j));
			}
		}

		try {
			List<Future<Integer>> futures = executor.invokeAll(callables);
			int futurePos = 0;
			for(int i = 0; i < matrix1.length; i++){
				for(int j = 0; j < matrix2[0].length; j++) {
					System.out.print(" " + futures.get(futurePos).get() + " ");
					futurePos++;
				}
				System.out.println();
			}
		} catch (Exception e) {

		}
		executor.shutdown();
	}
}

class MultiplyValue implements Callable<Integer> {
	int[][] matrix1;
	int[][] matrix2;
	int row;
	int col;

	public MultiplyValue(int[][] matrix1, int[][] matrix2, int row, int col) {
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.row = row;
		this.col = col;
	}

	@Override
	public Integer call() throws Exception {
		int value = 0;
		for(int i = 0; i < matrix1[row].length; i++) {
			value += matrix1[row][i] * matrix2[i][col];
		}
		return value;
	}
}