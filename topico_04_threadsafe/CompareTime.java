package topico_04_threadsafe;

import java.time.Duration;
import java.time.Instant;

/**
 * EXERCICIO 3 SLIDE 18
 * 
 * Para o exerc ́ıcio anterior, compare o desempenho medindo otempo de in ́ıcio
 * e t ́ermino para processar o intervalo.
 */

public class CompareTime {
    public static void main(String[] args) throws Exception {
        Instant start;
        Instant end;
        long duration;

        // Method lock
        start = Instant.now();
        new Primo2().getPrimos(7, new CounterMethodLock(0, 100000));
        end = Instant.now();
        duration = Duration.between(start, end).toMillis();
        System.out.println("Method lock: " + duration);

        // block lock
        start = Instant.now();
        new Primo2().getPrimos(7, new CounterBlockLock(0, 100000));
        end = Instant.now();
        duration = Duration.between(start, end).toMillis();
        System.out.println("Block Lock: " + duration);

        // Atomic
        start = Instant.now();
        new Primo2().getPrimos(7, new CounterAtomic(0, 100000));
        end = Instant.now();
        duration = Duration.between(start, end).toMillis();
        System.out.println("Atomic: " + duration);
    }
}