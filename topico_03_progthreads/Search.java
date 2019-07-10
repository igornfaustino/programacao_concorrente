package topico_03_progthreads;

/**
 * EXERCICIO 4 SLIDE 28
 * 
 * Faca um programa em Java que realize uma busca paralela em um vetor de
 * inteiros. Informe para o metodo: valor procurado, vetor de inteiros e o
 * numero de threads.
 */

/**
 * @author igornfaustino
 * 
 *         Search a value inside an array
 */
public class Search extends Thread {
    /**
     * Search an value with N threds
     * 
     * @param array      array to realize the search
     * @param tam        array size
     * @param value      value to get inside array
     * @param numThreads qtd of threds
     * @return index of value inside array.. (-1 if not found)
     */
    public int search(int[] array, int tam, int value, int numThreads) {
        ArrayIndex foundIndex = new ArrayIndex(-1);
        Thread[] threads = new Thread[numThreads];

        /**
         * ex.. 8/3 = 2.66 => 2 pos.. 0-2, 3-5, 6-8
         */
        int qtdPosPerThreads = tam / numThreads;
        int startPos = 0;
        int endPos = qtdPosPerThreads;

        for (int i = 0; i < numThreads - 1; i++) {
            threads[i] = new Thread(new SearchRunnable(array, startPos, endPos, value, foundIndex));
            startPos = endPos+1;
            endPos = startPos + qtdPosPerThreads;
        }
        threads[numThreads - 1] = new Thread(new SearchRunnable(array, startPos, tam, value, foundIndex));

        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return foundIndex.getIndex();
    }

    public static void main(String[] args) {
        int[] array = new int[10];
        System.out.println("[");
        for(int i = 0; i < 10; i++){
            array[i] = i*2;
            System.out.println(" " + array[i]);
        }
        System.out.println("]");

        int value = 4;
        System.out.println("index of value " + value + " is " + new Search().search(array, 10, value, 6));
    }
}

class SearchRunnable implements Runnable {
    int[] array;
    int startPos;
    int endPos;
    int value;
    ArrayIndex foundPos;

    SearchRunnable(int[] array, int startPos, int endPos, int value, ArrayIndex foundPos) {
        this.array = array;
        this.startPos = startPos;
        this.endPos = endPos;
        this.value = value;
        this.foundPos = foundPos;
    }

    @Override
    public void run() {
        for (int i = startPos; i < endPos; i++) {
            if (array[i] == value) {
                foundPos.setIndex(i);
            }
        }
    }
}

class ArrayIndex {
    int index;

    ArrayIndex(int idx) {
        index = idx;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }
}