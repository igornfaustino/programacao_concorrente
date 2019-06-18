package tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Fat {
	static long calcFat(int num) {
		long result = 1;
		for (int i = 1; i <= num; i++) {
			result *= i;
		}
		return result;
	}

	public static void main(String[] args) {
		MyValue value = new MyValue(0);
		Thread t = new Thread(new FatRunnable(value));
		t.start();

		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<Long> result = service.submit(new FatCallable());
		
		try {
			t.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(value.getValue());

		try {
			System.out.println(result.get().longValue());
		} catch (Exception e) {
			// TODO: handle exception
		}
		service.shutdown();
	}
}

class MyValue {
	long value;

	MyValue(long value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(long value) {
		this.value = value;
	}
}

class FatRunnable implements Runnable {
	MyValue value;

	FatRunnable(MyValue value) {
		this.value = value;
	}

	@Override
	public void run() {
		value.setValue(Fat.calcFat(15));
	}
}

class FatCallable implements Callable<Long> {
	@Override
	public Long call() {
		return Fat.calcFat(20);
	}
}