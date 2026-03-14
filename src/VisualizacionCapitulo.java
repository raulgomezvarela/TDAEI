import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class VisualizacionCapitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "fecha_visualizacion", nullable = false)
    protected LocalDate fechaVisualizacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "serie_id", nullable = false)
    protected Serie serie;

    @Column(name = "temporada", nullable = false)
    protected int temporada;

    @Column(name = "numero_capitulo", nullable = false)
    protected int numeroCapitulo;

    @Embedded
    protected Dinero importeAplicado;

    protected VisualizacionCapitulo() {
    }

    public VisualizacionCapitulo(LocalDate fechaVisualizacion, Serie serie, int temporada, int numeroCapitulo, Dinero importeAplicado) {
        if (fechaVisualizacion == null) {
            throw new IllegalArgumentException("La fecha de visualización no puede ser nula");
        }
        if (serie == null) {
            throw new IllegalArgumentException("La serie no puede ser nula");
        }
        if (temporada <= 0) {
            throw new IllegalArgumentException("La temporada debe ser mayor que 0");
        }
        if (numeroCapitulo <= 0) {
            throw new IllegalArgumentException("El número de capítulo debe ser mayor que 0");
        }
        if (importeAplicado == null) {
            throw new IllegalArgumentException("El importe aplicado no puede ser nulo");
        }

        serie.obtenerCapitulo(temporada, numeroCapitulo);

        this.fechaVisualizacion = fechaVisualizacion;
        this.serie = serie;
        this.temporada = temporada;
        this.numeroCapitulo = numeroCapitulo;
        this.importeAplicado = importeAplicado;
    }

    public static VisualizacionCapitulo crearDesde(Usuario usuario, Serie serie, int temporada, int numeroCapitulo, LocalDate fecha) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (serie == null) {
            throw new IllegalArgumentException("La serie no puede ser nula");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        Dinero importe;
        if (usuario.getTipoTarifa() == TipoTarifa.TARIFA_PLANA) {
            importe = new Dinero(0.0);
        } else {
            importe = serie.precioPorCapitulo();
        }

        return new VisualizacionCapitulo(fecha, serie, temporada, numeroCapitulo, importe);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFechaVisualizacion() {
        return fechaVisualizacion;
    }

    public Serie getSerie() {
        return serie;
    }

    public int getTemporada() {
        return temporada;
    }

    public int getNumeroCapitulo() {
        return numeroCapitulo;
    }

    public Dinero getImporteAplicado() {
        return importeAplicado;
    }
}
