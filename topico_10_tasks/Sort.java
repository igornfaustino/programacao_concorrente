package topico_10_tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.Future;

/**
 * EXERCICIO 6
 * 
 * Fa ̧ca um programa que execute trˆes algoritmos de ordena ̧c ̃ao para umconjunto de valores e exiba o resultado apenas do algoritmo que finalizarprimeiro (useinvokeAny)
 */

public class Sort {
	public static void main(String[] args) {
		Integer[] arr = new Integer[] { 5, 4, 3, 2, 1 };
		ExecutorService executor = Executors.newFixedThreadPool(3);

		List<Callable<Integer[]>> tasks = new ArrayList<Callable<Integer[]>>();
		tasks.add(new BubbleSort(arr));
		tasks.add(new SelectionSort(arr));
		tasks.add(new QuickSort(arr));
		try {
			Integer[] result = executor.invokeAny(tasks);
			System.out.println(Arrays.toString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}
}

class BubbleSort implements Callable<Integer[]> {
	Integer[] arr;

	BubbleSort(Integer[] arr) {
		this.arr = arr.clone();
	}

	@Override
	public Integer[] call() {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					Integer temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
		}
		return arr;
	}
}

class SelectionSort implements Callable<Integer[]> {
	Integer[] arr;

	SelectionSort(Integer[] arr) {
		this.arr = arr.clone();
	}

	@Override
	public Integer[] call() {
		for (int i = 0; i < arr.length; i++) {
			int smallerValuePos = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[smallerValuePos] > arr[j]) {
					smallerValuePos = j;
				}
			}
			Integer temp = arr[i];
			arr[i] = arr[smallerValuePos];
			arr[smallerValuePos] = temp;
		}
		return arr;
	}
}

class QuickSort implements Callable<Integer[]> {
	Integer[] arr;

	QuickSort(Integer[] arr) {
		this.arr = arr.clone();
	}

	void quickSort(Integer[] vet, int startPos, int endPos) {
		if (startPos < endPos) {
			int pivo = separa(vet, startPos, endPos);
			quickSort(vet, startPos, pivo - 1);
			quickSort(vet, pivo + 1, endPos);
		}
	}

	int separa(Integer[] vet, int startPos, int endPos) {
		int pivo = vet[startPos];
		int i = startPos + 1;
		int e = endPos;

		while (i <= e) {
			if (vet[i] <= pivo) {
				i++;
			} else if (pivo < vet[e]) {
				e--;
			} else {
				int temp = vet[i];
				vet[i] = vet[e];
				vet[e] = temp;
				i++;
				e--;
			}
		}
		vet[startPos] = vet[e];
		vet[e] = pivo;
		return e;
	}

	@Override
	public Integer[] call() {
		quickSort(arr, 0, arr.length - 1);
		return arr;
	}
}