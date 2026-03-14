import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "titulo", nullable = false, unique = true, length = 150)
    protected String titulo;

    @Column(name = "sinopsis", nullable = false, length = 2000)
    protected String sinopsis;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    protected CategoriaSerie categoria;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Temporada> temporadas;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<String> creadores;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<String> actoresPrincipales;

    protected Serie() {
        this.temporadas = new ArrayList<>();
        this.creadores = new ArrayList<>();
        this.actoresPrincipales = new ArrayList<>();
    }

    public Serie(String titulo, String sinopsis, CategoriaSerie categoria) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede ser nulo ni vacío");
        }
        if (sinopsis == null || sinopsis.isBlank()) {
            throw new IllegalArgumentException("La sinopsis no puede ser nula ni vacía");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }

        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.categoria = categoria;
        this.temporadas = new ArrayList<>();
        this.creadores = new ArrayList<>();
        this.actoresPrincipales = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public CategoriaSerie getCategoria() {
        return categoria;
    }

    public List<Temporada> getTemporadas() {
        return new ArrayList<>(temporadas);
    }

    public List<String> getCreadores() {
        return new ArrayList<>(creadores);
    }

    public List<String> getActoresPrincipales() {
        return new ArrayList<>(actoresPrincipales);
    }

    public void agregarTemporada(Temporada temporada) {
        if (temporada == null) {
            throw new IllegalArgumentException("La temporada no puede ser nula");
        }
        this.temporadas.add(temporada);
    }

    public void agregarCreador(String creador) {
        if (creador == null || creador.isBlank()) {
            throw new IllegalArgumentException("El creador no puede ser nulo ni vacío");
        }
        this.creadores.add(creador);
    }

    public void agregarActorPrincipal(String actor) {
        if (actor == null || actor.isBlank()) {
            throw new IllegalArgumentException("El actor no puede ser nulo ni vacío");
        }
        this.actoresPrincipales.add(actor);
    }

    public Capitulo obtenerCapitulo(int numeroTemporada, int numeroCapitulo) {
        Temporada temporada = temporadas.stream()
                .filter(t -> t.getNumero() == numeroTemporada)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe una temporada con ese número"));

        return temporada.obtenerCapitulo(numeroCapitulo);
    }

    public Dinero precioPorCapitulo() {
        return switch (categoria) {
            case ESTANDAR -> new Dinero(BigDecimal.valueOf(1.00));
            case SILVER -> new Dinero(BigDecimal.valueOf(1.50));
            case GOLD -> new Dinero(BigDecimal.valueOf(2.00));
        };
    }
}
