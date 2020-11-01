import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Cliente implements Runnable {

    private Supermercado supermercado;

    public Cliente(Supermercado supermercado) {
        this.supermercado = supermercado;
    }

    @Override
    public void run() {
        goToSupermarket();
        try {
            supermercado.goIn();
        } catch (InterruptedException e) {
            return;
        }
    }

    private void goToSupermarket() {
        System.out.printf("%s - The client %s is going to the Supermarket\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName());
    }
}
