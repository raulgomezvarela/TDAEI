import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    protected String titulo;

    @Column(name = "sinopsis", nullable = false, length = 2000)
    protected String sinopsis;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    protected CategoriaSerie categoria;

    @ElementCollection
    @CollectionTable(name = "serie_creadores", joinColumns = @JoinColumn(name = "serie_id"))
    @Column(name = "creador")
    protected List<String> creadores;

    @ElementCollection
    @CollectionTable(name = "serie_actores_principales", joinColumns = @JoinColumn(name = "serie_id"))
    @Column(name = "actor_principal")
    protected List<String> actoresPrincipales;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "serie_id")
    protected List<Temporada> temporadas;

    @Builder
    public Serie(String titulo, String sinopsis, CategoriaSerie categoria, List<String> creadores,
                 List<String> actoresPrincipales, List<Temporada> temporadas) {
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
        this.creadores = (creadores != null) ? creadores : new ArrayList<>();
        this.actoresPrincipales = (actoresPrincipales != null) ? actoresPrincipales : new ArrayList<>();
        this.temporadas = (temporadas != null) ? temporadas : new ArrayList<>();
    }

    public Capitulo obtenerCapitulo(int temporada, int numero) {
        for (Temporada t : temporadas) {
            if (t.numero == temporada) {
                return t.obtenerCapitulo(numero);
            }
        }
        throw new IllegalArgumentException("No existe la temporada indicada");
    }

    public Dinero precioPorCapitulo() {
        return switch (categoria) {
            case ESTANDAR -> Dinero.builder().euros(new java.math.BigDecimal("1.00")).build();
            case SILVER -> Dinero.builder().euros(new java.math.BigDecimal("1.50")).build();
            case GOLD -> Dinero.builder().euros(new java.math.BigDecimal("2.00")).build();
        };
    }
}