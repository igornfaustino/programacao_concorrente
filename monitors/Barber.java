package monitors;

import java.util.Random;

/**
 * Implemente uma solu ̧c ̃ao para o problema do BarbeiroDorminhoco usando
 * monitores.
 */

public class Barber extends Thread {
    Saloon barbearia;

    public Barber(Saloon barbearia) {
        this.barbearia = barbearia;
    }

    @Override
    public void run() {
        while(true) {
            barbearia.cortaCabelo();
        }
    }

    public static void main(String[] args) {
        Saloon barbearia = new Saloon();
        new Barber(barbearia).start();
        new Cliente(barbearia).start();
        new Cliente(barbearia).start();
        new Cliente(barbearia).start();
        new Cliente(barbearia).start();
        new Cliente(barbearia).start();
        new Cliente(barbearia).start();
        new Cliente(barbearia).start();
        new Cliente(barbearia).start();
    }
}

class Cliente extends Thread {
    Saloon barbearia;

    public Cliente(Saloon barbearia) {
        this.barbearia = barbearia;
    }

    @Override
    public void run() {
        barbearia.esperaVez();
    }
}

class Saloon {
    int chairs = 4;
    int qtdClientes = 0;
    boolean dormindo = true;
    boolean cortando = false;

    public synchronized void esperaVez() {
        System.out.println("Cliente entrou na barbearia");
        if (chairs == qtdClientes) {
            System.out.println("barbearia cheia... cliente foi embora");
            return;
        }

        qtdClientes++;

        while (cortando) {
            try {
                wait();
            } catch (Exception e) {
            }
        }

        if (dormindo) {
            System.out.println("cliente acordou o barbeiro");
            dormindo = false;
            notifyAll();
        }
    }

    public synchronized void cortaCabelo() {
        while (qtdClientes == 0) {
            try {
                System.out.println("Nenhum cliente... o barbeiro foi dormir");
                dormindo = true;
                wait();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        qtdClientes--;
        cortando = true;

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (Exception e) {
            // TODO: handle exception
        }

        System.out.println("barbeiro cortou o cabelo de um cliente");
        cortando = false;
        notifyAll();
    }
}