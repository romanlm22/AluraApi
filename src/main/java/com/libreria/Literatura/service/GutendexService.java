package com.libreria.Literatura.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.libreria.Literatura.model.Autor;
import com.libreria.Literatura.model.Libro;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GutendexService {

    private static final String API = "https://gutendex.com/books/";
    private static final int PAGE_SIZE = 32;
    private final RestTemplate rest = new RestTemplate();

    private volatile List<Libro> cacheLibros = new ArrayList<>();

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GutendexResponse {
        public String next;
        public List<Libro> results;
    }

    public synchronized void cargarTodosLosLibros() {
        if (!cacheLibros.isEmpty()) return;
        try {
            String url = API + "?page=1&page_size=" + PAGE_SIZE;
            GutendexResponse page = rest.getForObject(url, GutendexResponse.class);
            if (page != null && page.results != null) {
                cacheLibros = Collections.unmodifiableList(page.results);
                System.out.println("📚 Libros cargados (1ra página): " + cacheLibros.size());
            } else {
                cacheLibros = List.of();
                System.out.println("⚠️ No se recibieron resultados de Gutendex.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se pudieron cargar libros ahora: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    public List<Libro> getTodosLosLibros() {
        if (cacheLibros.isEmpty()) cargarTodosLosLibros();
        return cacheLibros;
    }

    public List<Autor> getTodosLosAutores() {
        return getTodosLosLibros().stream()
                .filter(l -> l.getAuthors() != null)
                .flatMap(l -> l.getAuthors().stream())
                .collect(Collectors.toMap(
                        Autor::getName, a -> a, (a, b) -> a, TreeMap::new
                ))
                .values().stream().toList();
    }

    public List<Libro> buscarPorTitulo(String texto) {
        String q = texto.toLowerCase(Locale.ROOT);
        return getTodosLosLibros().stream()
                .filter(l -> l.getTitle() != null && l.getTitle().toLowerCase(Locale.ROOT).contains(q))
                .toList();
    }

    public List<Libro> librosPorIdioma(String codigo) {
        String code = codigo.trim().toLowerCase(Locale.ROOT);
        return getTodosLosLibros().stream()
                .filter(l -> l.getLanguages() != null && l.getLanguages().stream()
                        .map(s -> s.toLowerCase(Locale.ROOT))
                        .anyMatch(code::equals))
                .toList();
    }

    public List<Autor> autoresVivosEnSiglo(int anio) {
        int fin = anio + 99;
        return getTodosLosAutores().stream()
                .filter(a -> {
                    Integer n = a.getBirthYear();
                    Integer m = a.getDeathYear();
                    boolean nAntesDelFin = (n == null) || (n <= fin);
                    boolean mDespuesInicio = (m == null) || (m >= anio);
                    return nAntesDelFin && mDespuesInicio;
                })
                .toList();
    }
}
