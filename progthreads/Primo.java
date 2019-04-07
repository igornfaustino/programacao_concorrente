package progthreads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Faca um programa em Java com threads que exiba os numeros primos entre 0 e
 * 100000
 */

public class Primo {
    public static void main(String[] args) {
        List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
        Thread[] threads = new Thread[7];
        int avaliableThreadIndex = -1;


        for (int i = 1; i < 100000; i += 2) {
            while(avaliableThreadIndex == -1){
                for(int idx = 0; idx < threads.length; idx++){
                    if(threads[idx] == null || threads[idx].isAlive()){
                        avaliableThreadIndex = idx;
                        break;
                    }
                }
            }
            threads[avaliableThreadIndex] = new CheckPrimoThread(list, i);
            threads[avaliableThreadIndex].start();
            avaliableThreadIndex = -1;
        }

        for(Thread t: threads){
            if(t != null) {
                while(t.isAlive());
            }
        }

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }
}

class CheckPrimoThread extends Thread {
    List primos;
    int value;

    CheckPrimoThread(List primos, int value) {
        this.value = value;
        this.primos = primos;
    }

    @Override
    public void run() {
        for (int i = 2; i < value; i++) {
            if (value % i == 0) {
                return;
            }
        }
        primos.add(value);
    }
}