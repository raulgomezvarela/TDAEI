import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Embedded;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    protected Usuario usuario;

    protected int anio;
    protected int mes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<LineaFactura> lineas;

    @Embedded
    protected Dinero total;

    protected Factura() {
        this.lineas = new ArrayList<>();
        this.total = new Dinero(0.0);
    }

    public Factura(Usuario usuario, int anio, int mes) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        }

        this.usuario = usuario;
        this.anio = anio;
        this.mes = mes;
        this.lineas = new ArrayList<>();
        this.total = new Dinero(0.0);
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public int getAnio() {
        return anio;
    }

    public int getMes() {
        return mes;
    }

    public List<LineaFactura> getLineas() {
        return new ArrayList<>(lineas);
    }

    public Dinero getTotal() {
        return total;
    }

    public Dinero calcularTotal(TipoTarifa tipoTarifa) {
        if (tipoTarifa == null) {
            throw new IllegalArgumentException("El tipo de tarifa no puede ser nulo");
        }

        if (tipoTarifa == TipoTarifa.TARIFA_PLANA) {
            this.total = new Dinero(20.0);
            return total;
        }

        Dinero suma = new Dinero(0.0);
        for (LineaFactura linea : lineas) {
            suma = suma.sumar(linea.getImporte());
        }
        this.total = suma;
        return total;
    }

    public void reconstruirDesde(List<VisualizacionCapitulo> visualizacionesMes, TipoTarifa tipoTarifa) {
        if (visualizacionesMes == null) {
            throw new IllegalArgumentException("La lista de visualizaciones no puede ser nula");
        }
        if (tipoTarifa == null) {
            throw new IllegalArgumentException("El tipo de tarifa no puede ser nulo");
        }

        this.lineas.clear();

        for (VisualizacionCapitulo visualizacion : visualizacionesMes) {
            Serie serie = visualizacion.getSerie();

            LineaFactura linea = new LineaFactura(
                    visualizacion.getFechaVisualizacion(),
                    serie.getTitulo(),
                    visualizacion.getTemporada(),
                    visualizacion.getNumeroCapitulo(),
                    visualizacion.getImporteAplicado()
            );

            this.lineas.add(linea);
        }

        calcularTotal(tipoTarifa);
    }

    public boolean correspondeA(int anio, int mes) {
        return this.anio == anio && this.mes == mes;
    }
}
