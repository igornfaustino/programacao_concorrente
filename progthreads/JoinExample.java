package progthreads;

import java.util.Scanner;

public class JoinExample extends Thread {
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Quantos numeros eu devo somar?");
        
        int qtdNums = scanner.nextInt();
        int resultSoma = 0;
                
        System.out.println("Quais sao os numeros?");
        for(int i = 0; i < qtdNums; i++) {
            resultSoma += scanner.nextInt();
        }
        System.out.println("resultado: " + resultSoma);
    }

    public static void main(String[] args) {
        Thread soma = new JoinExample();
        soma.start();
        System.out.println("Thread comecou a ser executada");
        try {
            soma.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Thread terminou de ser executada");
    }
}