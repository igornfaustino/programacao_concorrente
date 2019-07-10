package topico_03_progthreads;

/**
 * EXERCICIO 5 SLIDE 28
 * 
 * Fa ̧ca um programa multithreaded em Java que ordene umvetor usando o Merge
 * Sort recursivo. Fa ̧ca com que a threadprincipal dispare duas threads para
 * classificar cada metade dovetor
 */

public class MergeSort extends Thread {
    int[] array;
    int FirstStart;
    int FirstEnd;

    MergeSort() {
        init();
    }

    MergeSort(int[] array, int start, int end) {
        this.array = array;
        this.FirstStart = start;
        this.FirstEnd = end;
    }

    void merge(int start, int half, int end) {
        int size = end - start + 1;
        int firstArrayIdx = start;
        int secondArrayIdx = half + 1;
        int auxArrayIdx = 0;
        int[] auxArray = new int[size];

        while (firstArrayIdx <= half && secondArrayIdx <= end) {
            if (array[firstArrayIdx] < array[secondArrayIdx]) {
                // if first array element is small.. put this on aux
                auxArray[auxArrayIdx] = array[firstArrayIdx];
                firstArrayIdx++;
            } else {
                auxArray[auxArrayIdx] = array[secondArrayIdx];
                secondArrayIdx++;
            }
            auxArrayIdx++;
        }

        // if first half not ended
        while (firstArrayIdx <= half) {
            auxArray[auxArrayIdx] = array[firstArrayIdx];
            firstArrayIdx++;
            auxArrayIdx++;
        }

        // if second half not ended
        while (secondArrayIdx <= end) {
            auxArray[auxArrayIdx] = array[secondArrayIdx];
            secondArrayIdx++;
            auxArrayIdx++;
        }

        // put all elements from aux to orignal array
        auxArrayIdx = 0;
        for (int i = start; i <= end; i++) {
            array[i] = auxArray[auxArrayIdx];
            auxArrayIdx++;
        }
    }

    void mergeSort(int start, int end) {
        if (start < end) {
            int half = (start + end) / 2;

            mergeSort(start, half);
            mergeSort(half + 1, end);

            merge(start, half, end);
        }
    }

    void init() {
        int size = 100;
        array = new int[size];

        for (int i = size - 1; i >= 0; i--) {
            array[size - i - 1] = i;
        }

        int half = (size - 1) / 2;
        Thread firstHalf = new MergeSort(array, 0, half);
        Thread secondHalf = new MergeSort(array, half + 1, size - 1);

        firstHalf.start();
        secondHalf.start();

        try {
            firstHalf.join();
            secondHalf.join();
        } catch (Exception e) {
            // TODO: handle exception
        }

        merge(0, half, size - 1);

        for (int i : array) {
            System.out.println(i);
        }
    }

    @Override
    public void run() {
        mergeSort(FirstStart, FirstEnd);
    }

    public static void main(String[] args) {
        new MergeSort();
    }
}