package onedimensionalstencil;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LatchSolution implements Runnable{
	static boolean hasChange;
	static float[] array = new float[] { 1, 2, 2, 1 };
	static float[] newArray = array.clone();
	int pos;
	CountDownLatch latch;

	LatchSolution(int pos, CountDownLatch latch) {
		this.pos = pos;
		this.latch = latch;
	}

	@Override
	public void run() {
		newArray[pos] = (array[pos - 1] + array[pos + 1]) / 2;
		hasChange = hasChange || (newArray[pos] != array[pos]);
		latch.countDown();
	}

	public static void main(String[] args) {
		int numIterations = 0;
		ExecutorService executor = Executors.newFixedThreadPool(array.length - 2);
		
		do {
			CountDownLatch latch = new CountDownLatch(array.length - 2);
			hasChange = false;
			numIterations++;
			for (int i = 1; i < array.length - 1; i++) {
				executor.submit(new LatchSolution(i, latch));
			}
			try {
				latch.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
			array = newArray.clone();
		} while (hasChange);

		System.out.println("numero de iterações: " + numIterations);
		System.out.println(Arrays.toString(array));
		executor.shutdown();
	}
}