import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Capitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "numero", nullable = false)
    protected int numero;

    @Column(name = "titulo", nullable = false, length = 150)
    protected String titulo;

    @Column(name = "descripcion", nullable = false, length = 1000)
    protected String descripcion;

    protected Capitulo() {
    }

    public Capitulo(int numero, String titulo, String descripcion) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de capítulo debe ser mayor que 0");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede ser nulo ni vacío");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula ni vacía");
        }

        this.numero = numero;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void cambiarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede ser nulo ni vacío");
        }
        this.titulo = titulo;
    }

    public void cambiarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula ni vacía");
        }
        this.descripcion = descripcion;
    }
}
