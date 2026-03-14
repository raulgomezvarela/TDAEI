import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineaFactura {

    protected LocalDate fechaVisualizacion;
    protected String tituloSerie;
    protected int temporada;
    protected int numeroCapitulo;

    @Embedded
    protected Dinero importe;

    @Builder
    public LineaFactura(LocalDate fechaVisualizacion, String tituloSerie, int temporada, int numeroCapitulo, Dinero importe) {
        if (fechaVisualizacion == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (tituloSerie == null || tituloSerie.isBlank()) {
            throw new IllegalArgumentException("El título de serie no puede ser nulo ni vacío");
        }
        if (temporada <= 0 || numeroCapitulo <= 0) {
            throw new IllegalArgumentException("Temporada y número de capítulo deben ser mayores que 0");
        }
        if (importe == null) {
            throw new IllegalArgumentException("El importe no puede ser nulo");
        }

        this.fechaVisualizacion = fechaVisualizacion;
        this.tituloSerie = tituloSerie;
        this.temporada = temporada;
        this.numeroCapitulo = numeroCapitulo;
        this.importe = importe;
    }
}