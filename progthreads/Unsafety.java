package progthreads;

import java.util.ArrayList;

/**
 * Unsafety
 */
public class Unsafety {
    public static void main(String[] args) {
        ArrayList oldValues = new ArrayList<Integer>();
        UnsafetyCount count = new UnsafetyCount();
        for (int i = 0; i < 4; i++) {
            new Thread(new UnsafetyRunnable(count, oldValues)).start();
        }
    }
}

class UnsafetyRunnable implements Runnable {
    UnsafetyCount count;
    ArrayList oldValues;

    UnsafetyRunnable(UnsafetyCount count, ArrayList oldValues) {
        this.count = count;
        this.oldValues = oldValues;
    }

    @Override
    public void run() {
        while (true){
            int value = this.count.getNextValue();
            System.out.println(Thread.currentThread().getName() + ": " + value);
            if (oldValues.contains(value)) {
                System.out.println("INCONSISTENCIA DETECTADA");
                System.exit(1);
            } else {
                oldValues.add(value);
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }
}

class UnsafetyCount {
    private int value = 0;

    /**
     * return the current value and update for next call
     * @return the value
     */
    public int getNextValue() {
        return value++;
    }
}