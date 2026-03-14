import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Temporada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "numero", nullable = false)
    protected int numero;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Capitulo> capitulos;

    protected Temporada() {
        this.capitulos = new ArrayList<>();
    }

    public Temporada(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de temporada debe ser mayor que 0");
        }
        this.numero = numero;
        this.capitulos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public List<Capitulo> getCapitulos() {
        return new ArrayList<>(capitulos);
    }

    public void agregarCapitulo(Capitulo capitulo) {
        if (capitulo == null) {
            throw new IllegalArgumentException("El capítulo no puede ser nulo");
        }
        this.capitulos.add(capitulo);
    }

    public Capitulo obtenerCapitulo(int numero) {
        return capitulos.stream()
                .filter(capitulo -> capitulo.getNumero() == numero)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe un capítulo con ese número"));
    }
}
