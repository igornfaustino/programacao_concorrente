package onedimensionalstencil;

import java.util.Arrays;

public class SequencialSolution {
	public static void main(String[] args) {
		float[] array = new float[] { 1, 2, 2, 1};
		float[] newArray = array.clone();

		boolean hasChange;
		int numIterations = 0;

		do {
			hasChange = false;
			numIterations++;
			for (int i = 1; i < array.length - 1; i++) {
				newArray[i] = (array[i-1]+array[i+1])/2;
				hasChange = hasChange || (newArray[i] != array[i]);
			}
			array = newArray.clone();
		} while (hasChange);

		System.out.println("numero de iterações: " + numIterations);
		System.out.println(Arrays.toString(array));
	}
}