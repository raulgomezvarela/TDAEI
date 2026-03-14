import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Capitulo(int numero, String titulo, String descripcion) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número debe ser mayor que 0");
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
}