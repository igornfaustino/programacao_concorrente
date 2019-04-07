package progthreads;

/**
 * Fa ̧ca um programa em Java para testar um conjunto deopera ̧c ̃oes sobre um
 * grupo de threads. Use o ThreadGroup.
 */

public class GroupThreads {
    public static void main(String[] args) {
        ThreadGroup group = new ThreadGroup("myGroup");

        for(int i = 0; i < 10; i++){
            new Thread(group, ()->{
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    //TODO: handle exception
                }
            }).start();
        }

        System.out.println("Num de threads ativas: " + group.activeCount());
        System.out.println("Daemon: " + group.isDaemon());
        System.out.println("prioridade maxima: " + group.getMaxPriority());
        System.out.println("interrompendo threads...");
        group.interrupt();
        System.out.println("Num de threads ativas: " + group.activeCount());
    }
}
