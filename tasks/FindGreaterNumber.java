package tasks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.Future;

public class FindGreaterNumber {
	public static void main(String[] args) {
		int nThreads = 4;
		int nTasks = 8;
		int[] array = new int[] { 3, 4, 5, 1, -2, 4, 10, 12, -69 };
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		int qtdPosPerTask = array.length / nTasks;
		int startPos = 0;

		Set<Callable<Integer>> callables = new HashSet<Callable<Integer>>();
		for(int i = 1; i<nTasks; i++){
			callables.add(new GreaterNumber(array, startPos, startPos+qtdPosPerTask-1));
			startPos += qtdPosPerTask;
		}
		callables.add(new GreaterNumber(array, startPos, array.length-1));

		try{
			List<Future<Integer>> futures = executor.invokeAll(callables);

			int greaterNumber = futures.get(0).get();
			for(int i = 1; i < futures.size(); i++) {
				int val = futures.get(i).get();
				if(greaterNumber < val)
					greaterNumber = val;
			}
			System.out.println(greaterNumber);
		} catch(Exception e) {

		}
		executor.shutdown();
	}
}

class GreaterNumber implements Callable<Integer> {
	int startPos;
	int[] array;
	int endPos;

	public GreaterNumber(int[] array, int startPos, int endPos) {
		this.array = array;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	@Override
	public Integer call() throws Exception {
		int greaterNumberFounded = array[startPos];
		for (int i = startPos + 1; i <= endPos; i++) {
			if (greaterNumberFounded < array[i])
				greaterNumberFounded = array[i];
		}
		return greaterNumberFounded;
	}
}