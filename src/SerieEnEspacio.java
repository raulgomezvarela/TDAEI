import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class SerieEnEspacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "serie_id", nullable = false)
    protected Serie serie;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    protected EstadoSerieUsuario estado;

    @Embedded
    protected ReferenciaCapitulo capituloMasAltoVisto;

    @ElementCollection
    @CollectionTable(name = "serie_en_espacio_capitulos_vistos", joinColumns = @JoinColumn(name = "serie_en_espacio_id"))
    protected Set<ReferenciaCapitulo> capitulosVistos;

    protected SerieEnEspacio() {
        this.capitulosVistos = new HashSet<>();
        this.estado = EstadoSerieUsuario.PENDIENTE;
    }

    public SerieEnEspacio(Serie serie) {
        if (serie == null) {
            throw new IllegalArgumentException("La serie no puede ser nula");
        }

        this.serie = serie;
        this.estado = EstadoSerieUsuario.PENDIENTE;
        this.capitulosVistos = new HashSet<>();
        this.capituloMasAltoVisto = null;
    }

    public Long getId() {
        return id;
    }

    public Serie getSerie() {
        return serie;
    }

    public EstadoSerieUsuario getEstado() {
        return estado;
    }

    public ReferenciaCapitulo getCapituloMasAltoVisto() {
        return capituloMasAltoVisto;
    }

    public Set<ReferenciaCapitulo> getCapitulosVistos() {
        return new HashSet<>(capitulosVistos);
    }

    public void marcarVisto(int temporada, int numeroCapitulo) {
        ReferenciaCapitulo referencia = new ReferenciaCapitulo(temporada, numeroCapitulo);

        serie.obtenerCapitulo(temporada, numeroCapitulo);

        this.capitulosVistos.add(referencia);

        if (this.capituloMasAltoVisto == null || referencia.esPosteriorA(this.capituloMasAltoVisto)) {
            this.capituloMasAltoVisto = referencia;
        }

        actualizarEstadoSiProcede();
    }

    public void actualizarEstadoSiProcede() {
        if (capitulosVistos.isEmpty()) {
            this.estado = EstadoSerieUsuario.PENDIENTE;
            return;
        }

        if (esTerminada()) {
            this.estado = EstadoSerieUsuario.TERMINADA;
        } else {
            this.estado = EstadoSerieUsuario.EMPEZADA;
        }
    }

    public boolean esTerminada() {
        Temporada ultimaTemporada = serie.getTemporadas().stream()
                .max((t1, t2) -> Integer.compare(t1.getNumero(), t2.getNumero()))
                .orElseThrow(() -> new IllegalStateException("La serie no tiene temporadas"));

        Capitulo ultimoCapitulo = ultimaTemporada.getCapitulos().stream()
                .max((c1, c2) -> Integer.compare(c1.getNumero(), c2.getNumero()))
                .orElseThrow(() -> new IllegalStateException("La temporada no tiene capítulos"));

        ReferenciaCapitulo ultimo = new ReferenciaCapitulo(ultimaTemporada.getNumero(), ultimoCapitulo.getNumero());

        return capitulosVistos.contains(ultimo);
    }

    public int obtenerTemporadaInicialParaAbrir() {
        if (estado == EstadoSerieUsuario.EMPEZADA && capituloMasAltoVisto != null) {
            return capituloMasAltoVisto.getTemporada();
        }

        return 1;
    }

}
