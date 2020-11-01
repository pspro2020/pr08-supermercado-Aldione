import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Supermercado {

    private final int numeroCajas;
    private final boolean[] cajas;
    private Semaphore semaphore;
    private ReentrantLock reentrantLock = new ReentrantLock(true);

    public Supermercado(int numeroCajas) {
        this.numeroCajas = numeroCajas;
        semaphore = new Semaphore(numeroCajas, true);
        cajas = new boolean[numeroCajas];
        for (int i = 0; i < numeroCajas; i++) {
            cajas[i] = true;
        }
    }

    public void goIn() throws InterruptedException {
        shopping();
    }

    private void shopping() throws InterruptedException {
        System.out.printf("%s - The client %s is buying\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3) + 1);
        goToPay();
    }

    public void goToPay() throws InterruptedException {
        System.out.printf("%s - The client %s wants to go to the check-out\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName());
        semaphore.acquire();
        try {
            int caja = selectCaja();
            if (caja >= 0) {
                pay(caja);
                leave(caja);
            }
        } finally {
            semaphore.release();
        }
    }

    private void leave(int caja) {
        cajas[caja] = true;
        System.out.printf("%s - The client %s just paid at the check-out %d and left\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName(), caja + 1);
    }

    private void pay(int caja) throws InterruptedException {
        System.out.printf("%s - The client %s is paying at the check-out %d\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName(), caja + 1);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(4) + 1);
    }

    private int selectCaja() {
        reentrantLock.lock();
        try {
            for (int i = 0; i < numeroCajas; i++) {
                if (cajas[i]) {
                    cajas[i] = false;
                    return i;
                }
            }
            return -1;
        } finally {
            reentrantLock.unlock();
        }
    }
}
