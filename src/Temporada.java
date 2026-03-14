import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Temporada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "numero", nullable = false)
    protected int numero;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "temporada_id")
    protected List<Capitulo> capitulos;

    @Builder
    public Temporada(int numero, List<Capitulo> capitulos) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de temporada debe ser mayor que 0");
        }
        this.numero = numero;
        this.capitulos = (capitulos != null) ? capitulos : new ArrayList<>();
    }

    public Capitulo obtenerCapitulo(int numero) {
        for (Capitulo capitulo : capitulos) {
            if (capitulo.numero == numero) {
                return capitulo;
            }
        }
        throw new IllegalArgumentException("No existe un capítulo con ese número");
    }
}