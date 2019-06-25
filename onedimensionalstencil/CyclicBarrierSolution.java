package onedimensionalstencil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrierSolution implements Runnable {
	static boolean hasChange;
	static boolean doLoop;
	static float[] array = new float[] { 1, 2, 2, 1 };
	static float[] newArray = array.clone();
	static AtomicInteger numIterations = new AtomicInteger(0);
	static CyclicBarrier barrier;
	static CyclicBarrier start = new CyclicBarrier(array.length - 2);
	int pos;

	CyclicBarrierSolution(int pos) {
		this.pos = pos;
	}

	@Override
	public void run() {
		do {
			newArray[pos] = (array[pos - 1] + array[pos + 1]) / 2;
			hasChange = hasChange || (newArray[pos] != array[pos]);
			try {
				barrier.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (doLoop);
	}

	public static void main(String[] args) {
		barrier = new CyclicBarrier(array.length - 2, new Runnable(){
			@Override
			public void run() {
				numIterations.incrementAndGet();
				array = newArray.clone();
				doLoop = hasChange;
				hasChange = false;
			}
		});

		Thread[] t = new Thread[array.length - 2];
		for (int i = 1; i < array.length - 1; i++) {
			t[i-1] = new Thread(new CyclicBarrierSolution(i));
			t[i-1].start();
		}

		for (int i = 1; i < array.length - 1; i++) {
			try {
				t[i-1].join();
			} catch (Exception e) {
				//TODO: handle exception
			}
		}

		System.out.println("numero de iterações: " + numIterations);
		System.out.println(Arrays.toString(array));
	}
}