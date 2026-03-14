import jakarta.persistence.Embedded;

import java.time.LocalDate;
import java.util.Objects;

public class LineaFactura {

    protected LocalDate fechaVisualizacion;
    protected String tituloSerie;
    protected int temporada;
    protected int numeroCapitulo;

    @Embedded
    protected Dinero importe;

    protected LineaFactura() {
    }

    public LineaFactura(LocalDate fechaVisualizacion, String tituloSerie, int temporada, int numeroCapitulo, Dinero importe) {
        if (fechaVisualizacion == null) {
            throw new IllegalArgumentException("La fecha de visualización no puede ser nula");
        }
        if (tituloSerie == null || tituloSerie.isBlank()) {
            throw new IllegalArgumentException("El título de la serie no puede ser nulo ni vacío");
        }
        if (temporada <= 0) {
            throw new IllegalArgumentException("La temporada debe ser mayor que 0");
        }
        if (numeroCapitulo <= 0) {
            throw new IllegalArgumentException("El número de capítulo debe ser mayor que 0");
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

    public LocalDate getFechaVisualizacion() {
        return fechaVisualizacion;
    }

    public String getTituloSerie() {
        return tituloSerie;
    }

    public int getTemporada() {
        return temporada;
    }

    public int getNumeroCapitulo() {
        return numeroCapitulo;
    }

    public Dinero getImporte() {
        return importe;
    }
}
