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
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 100)
    protected String nombreUsuario;

    @Column(name = "contrasena_hash", nullable = false, length = 255)
    protected String contrasenaHash;

    @Embedded
    protected Iban iban;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tarifa", nullable = false)
    protected TipoTarifa tipoTarifa;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    protected List<SerieEnEspacio> seriesEnMiEspacio;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    protected List<VisualizacionCapitulo> visualizaciones;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    protected List<Factura> facturas;

    protected Usuario() {
        this.seriesEnMiEspacio = new ArrayList<>();
        this.visualizaciones = new ArrayList<>();
        this.facturas = new ArrayList<>();
    }

    public Usuario(String nombreUsuario, String contrasenaHash, Iban iban, TipoTarifa tipoTarifa) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo ni vacío");
        }
        if (contrasenaHash == null || contrasenaHash.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula ni vacía");
        }
        if (iban == null) {
            throw new IllegalArgumentException("El IBAN no puede ser nulo");
        }
        if (tipoTarifa == null) {
            throw new IllegalArgumentException("El tipo de tarifa no puede ser nulo");
        }

        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.iban = iban;
        this.tipoTarifa = tipoTarifa;
        this.seriesEnMiEspacio = new ArrayList<>();
        this.visualizaciones = new ArrayList<>();
        this.facturas = new ArrayList<>();
    }

    public void agregarSerie(Serie serie) {
        if (serie == null) {
            throw new IllegalArgumentException("La serie no puede ser nula");
        }

        boolean yaExiste = this.seriesEnMiEspacio.stream()
                .anyMatch(serieEnEspacio -> Objects.equals(serieEnEspacio.getSerie(), serie));

        if (yaExiste) {
            throw new IllegalArgumentException("La serie ya está en el espacio del usuario");
        }

        this.seriesEnMiEspacio.add(new SerieEnEspacio(serie));
    }

    public void marcarCapituloComoVisto(Long serieId, int temporada, int numeroCapitulo, LocalDate fecha) {
        if (serieId == null) {
            throw new IllegalArgumentException("El id de la serie no puede ser nulo");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        SerieEnEspacio serieEnEspacio = this.seriesEnMiEspacio.stream()
                .filter(se -> se.getSerie().getId() != null && se.getSerie().getId().equals(serieId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La serie no está en el espacio del usuario"));

        serieEnEspacio.marcarVisto(temporada, numeroCapitulo);

        VisualizacionCapitulo visualizacion = VisualizacionCapitulo.crearDesde(
                this,
                serieEnEspacio.getSerie(),
                temporada,
                numeroCapitulo,
                fecha
        );

        this.visualizaciones.add(visualizacion);
    }

    public List<SerieEnEspacio> obtenerSeriesPendientes() {
        return this.seriesEnMiEspacio.stream()
                .filter(serieEnEspacio -> serieEnEspacio.getEstado() == EstadoSerieUsuario.PENDIENTE)
                .collect(Collectors.toList());
    }

    public List<SerieEnEspacio> obtenerSeriesEmpezadas() {
        return this.seriesEnMiEspacio.stream()
                .filter(serieEnEspacio -> serieEnEspacio.getEstado() == EstadoSerieUsuario.EMPEZADA)
                .collect(Collectors.toList());
    }

    public List<SerieEnEspacio> obtenerSeriesTerminadas() {
        return this.seriesEnMiEspacio.stream()
                .filter(serieEnEspacio -> serieEnEspacio.getEstado() == EstadoSerieUsuario.TERMINADA)
                .collect(Collectors.toList());
    }

    public Dinero calcularImporteMes(int anio, int mes) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        }

        if (this.tipoTarifa == TipoTarifa.TARIFA_PLANA) {
            return new Dinero(20.0);
        }

        Dinero total = new Dinero(0.0);

        for (VisualizacionCapitulo visualizacion : visualizaciones) {
            if (visualizacion.getFechaVisualizacion().getYear() == anio
                    && visualizacion.getFechaVisualizacion().getMonthValue() == mes) {
                total = total.sumar(visualizacion.getImporteAplicado());
            }
        }

        return total;
    }

    public Factura generarFacturaMes(int anio, int mes) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        }

        List<VisualizacionCapitulo> visualizacionesMes = this.visualizaciones.stream()
                .filter(v -> v.getFechaVisualizacion().getYear() == anio
                        && v.getFechaVisualizacion().getMonthValue() == mes)
                .collect(Collectors.toList());

        Factura factura = new Factura(this, anio, mes);
        factura.reconstruirDesde(visualizacionesMes, this.tipoTarifa);

        this.facturas.removeIf(f -> f.correspondeA(anio, mes));
        this.facturas.add(factura);

        return factura;
    }
}
