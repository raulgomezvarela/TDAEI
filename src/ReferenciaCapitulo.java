import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ReferenciaCapitulo {

    @Column(name = "temporada", nullable = false)
    protected int temporada;

    @Column(name = "numero_capitulo", nullable = false)
    protected int numeroCapitulo;

    protected ReferenciaCapitulo() {
    }

    public ReferenciaCapitulo(int temporada, int numeroCapitulo) {
        if (temporada <= 0) {
            throw new IllegalArgumentException("La temporada debe ser mayor que 0");
        }
        if (numeroCapitulo <= 0) {
            throw new IllegalArgumentException("El número de capítulo debe ser mayor que 0");
        }
        this.temporada = temporada;
        this.numeroCapitulo = numeroCapitulo;
    }

    public int getTemporada() {
        return temporada;
    }

    public int getNumeroCapitulo() {
        return numeroCapitulo;
    }

    public boolean esPosteriorA(ReferenciaCapitulo otra) {
        if (otra == null) {
            throw new IllegalArgumentException("La referencia a comparar no puede ser nula");
        }
        if (this.temporada != otra.temporada) {
            return this.temporada > otra.temporada;
        }
        return this.numeroCapitulo > otra.numeroCapitulo;
    }
}
