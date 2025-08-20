package com.libreria.Literatura.ui;

import com.libreria.Literatura.model.Autor;
import com.libreria.Literatura.model.Libro;
import com.libreria.Literatura.service.GutendexService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Consola {

    private final GutendexService service;
    private final Scanner sc = new Scanner(System.in);

    public Consola(GutendexService service) {
        this.service = service;
    }

    public void mostrarMenu() {
        int opcion = -1;
        do {
            System.out.println("------------");
            System.out.println("Elija la opción a través de su número:");
            System.out.println("1- Buscar libro por título");
            System.out.println("2- Listar libros registrados");
            System.out.println("3- Listar autores registrados");
            System.out.println("4- Listar autores vivos en un determinado año");
            System.out.println("5- Listar libros por idioma");
            System.out.println("0- Salir");
            System.out.print("Elija una opción: ");

            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Opción inválida.");
                continue;
            }
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> opcionBuscarPorTitulo();
                case 2 -> opcionListarLibros();
                case 3 -> opcionListarAutores();    // funciona incluso la primera vez
                case 4 -> opcionAutoresVivosEnSiglo();
                case 5 -> opcionLibrosPorIdioma();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void opcionBuscarPorTitulo() {
        System.out.print("Ingrese parte/todo del título: ");
        String q = sc.nextLine().trim();
        List<Libro> libros = service.buscarPorTitulo(q);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros con ese texto.");
            return;
        }
        imprimirLibros(libros);
    }

    private void opcionListarLibros() {
        List<Libro> libros = service.getTodosLosLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay libros (¿problema de conexión?).");
            return;
        }
        imprimirLibros(libros);
    }

    private void opcionListarAutores() {
        List<Autor> autores = service.getTodosLosAutores();
        if (autores.isEmpty()) {
            System.out.println("No hay autores (¿problema de conexión?).");
            return;
        }
        for (Autor a : autores) {
            System.out.println("Autor: " + a.getName());
            System.out.println("  Nacimiento: " + (a.getBirthYear() != null ? a.getBirthYear() : "?"));
            System.out.println("  Fallecimiento: " + (a.getDeathYear() != null ? a.getDeathYear() : "?"));
            System.out.println();
        }
        System.out.println("Total autores: " + autores.size());
    }

    private void opcionAutoresVivosEnSiglo() {
        System.out.print("Ingrese el año de inicio del siglo (ej: 1600): ");
        if (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.println("Año inválido.");
            return;
        }
        int anio = sc.nextInt();
        sc.nextLine();

        List<Autor> autores = service.autoresVivosEnSiglo(anio);
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos entre " + anio + " y " + (anio + 99));
            return;
        }
        for (Autor a : autores) {
            System.out.println("Autor: " + a.getName());
            System.out.println("  Nacimiento: " + (a.getBirthYear() != null ? a.getBirthYear() : "?"));
            System.out.println("  Fallecimiento: " + (a.getDeathYear() != null ? a.getDeathYear() : "?"));
            System.out.println();
        }
        System.out.println("Total autores en ese siglo: " + autores.size());
    }

    private void opcionLibrosPorIdioma() {
        System.out.print("Ingrese el idioma (código ISO: es/en/fr/pt/...): ");
        String code = sc.nextLine().trim();
        List<Libro> libros = service.librosPorIdioma(code);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en idioma '" + code + "'.");
            return;
        }
        imprimirLibros(libros);
    }

    private void imprimirLibros(List<Libro> libros) {
        for (Libro l : libros) {
            System.out.println("📖 " + l.getTitle());
            if (l.getAuthors() != null && !l.getAuthors().isEmpty()) {
                System.out.print("   Autor(es): ");
                for (int i = 0; i < l.getAuthors().size(); i++) {
                    System.out.print(l.getAuthors().get(i).getName());
                    if (i < l.getAuthors().size() - 1) System.out.print(" | ");
                }
                System.out.println();
            }
            System.out.println("   Idiomas: " + (l.getLanguages() != null ? l.getLanguages() : "[]"));
            System.out.println("   Descargas: " + (l.getDownloadCount() != null ? l.getDownloadCount() : 0));
            System.out.println("---------------");
        }
        System.out.println("Total libros: " + libros.size());
    }
}
