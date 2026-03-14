import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenciaCapitulo {

    @Column(name = "temporada", nullable = false)
    protected int temporada;

    @Column(name = "numero_capitulo", nullable = false)
    protected int numeroCapitulo;

    @Builder
    public ReferenciaCapitulo(int temporada, int numeroCapitulo) {
        if (temporada <= 0 || numeroCapitulo <= 0) {
            throw new IllegalArgumentException("Temporada y número de capítulo deben ser mayores que 0");
        }
        this.temporada = temporada;
        this.numeroCapitulo = numeroCapitulo;
    }
}