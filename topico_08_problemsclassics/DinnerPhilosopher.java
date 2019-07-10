package topico_08_problemsclassics;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * EXERCICIO 2 SLIDE 22
 * 
 * Implementar 3 solu ̧c ̃oes distintas para o jantar dos fil ́osofosque n ̃ao causem deadlock
 */

public class DinnerPhilosopher {
    public static void main(String[] args) {
        DiningResource diningResource = new SolutionThree(5);
        new Philosopher(0, diningResource).start();
        new Philosopher(1, diningResource).start();
        new Philosopher(2, diningResource).start();
        new Philosopher(3, diningResource).start();
        new Philosopher(4, diningResource).start();
    }
}

abstract class DiningResource {
    int numberResources = 0;
    Semaphore[] fork = null;

    public DiningResource(int numberResources) {
        this.numberResources = numberResources;
        fork = new Semaphore[numberResources];
        for (int i = 0; i < numberResources; i++) {
            fork[i] = new Semaphore(1);
        }
    }

    public abstract void take(int idPhilosopher);

    public abstract void release(int idPhilosopher);
}

class SolutionOne extends DiningResource {
    int lastGuy = 0;

    public SolutionOne(int numberResources) {
        super(numberResources);
        lastGuy = numberResources - 1;
    }

    @Override
    public void take(int idPhilosopher) {
        try {
            if (idPhilosopher != lastGuy) {
                fork[idPhilosopher].acquire();
                fork[(idPhilosopher + 1) % numberResources].acquire();
            } else {
                fork[(idPhilosopher + 1) % numberResources].acquire();
                fork[idPhilosopher].acquire();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release(int idPhilosopher) {
        fork[idPhilosopher].release();
        fork[(idPhilosopher + 1) % numberResources].release();
    }
}

class SolutionTwo extends DiningResource {
    Semaphore mutex = new Semaphore(1);

    public SolutionTwo(int numberResources) {
        super(numberResources);
    }

    @Override
    public void take(int idPhilosopher) {
        try {
            mutex.acquire();
            fork[idPhilosopher].acquire();
            fork[(idPhilosopher + 1) % numberResources].acquire();
            mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release(int idPhilosopher) {
        fork[idPhilosopher].release();
        fork[(idPhilosopher + 1) % numberResources].release();
    }
}

class SolutionThree extends DiningResource {
    Semaphore mutex = new Semaphore(1);
    int numberPhilosophersTryingToGetFork = 0;

    public SolutionThree(int numberResources) {
        super(numberResources);
    }

    @Override
    public void take(int idPhilosopher) {
        try {
            mutex.acquire();
            numberPhilosophersTryingToGetFork++;
            if (numberPhilosophersTryingToGetFork == numberResources) {
                mutex.release();
                Thread.sleep(1000);
            } else {
                mutex.release();
            }
            fork[idPhilosopher].acquire();
            fork[(idPhilosopher + 1) % numberResources].acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release(int idPhilosopher) {
        try {
            mutex.acquire();
            numberPhilosophersTryingToGetFork--;
            mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fork[idPhilosopher].release();
        fork[(idPhilosopher + 1) % numberResources].release();
    }
}

class Philosopher extends Thread {
    int id = 0;
    DiningResource diningResource;

    Philosopher(int id, DiningResource diningResource) {
        this.id = id;
        this.diningResource = diningResource;
    }

    @Override
    public void run() {
        while (true) {
            diningResource.take(id);
            try {
                Thread.sleep(new Random().nextInt(5000));
                System.out.println("filosofo " + id + " pegou os garfos");
            } catch (Exception e) {
                // TODO: handle exception
            }
            diningResource.release(id);
        }
    }
}