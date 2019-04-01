/** Implemente o exemplo anterior usando Lambda Expression. */

package progthreads;

// import java.lang.Runnable;
// import java.lang.Thread;

public class LambdaThread {
    public static void main(String[] args) {
        new Thread(() -> System.out.println("Hello from a thread")).start();
    }
}
