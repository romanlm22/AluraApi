package com.libreria.Literatura;

import com.libreria.Literatura.ui.Consola;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.libreria")
public class LiteraturaApplication implements CommandLineRunner {

    private final Consola consola;

    public LiteraturaApplication(Consola consola) {
        this.consola = consola;
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(LiteraturaApplication.class, args);
        } catch (Throwable t) {
            System.out.println("💥 Error al iniciar SpringApplication: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            t.printStackTrace();
        }
    }

    @Override
    public void run(String... args) {
        try {
            consola.mostrarMenu();
        } catch (Throwable t) {
            System.out.println("💥 Error ejecutando el menú: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            t.printStackTrace();
        }
    }
}
