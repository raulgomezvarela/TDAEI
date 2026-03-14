import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class Dinero {

    @Column(name = "euros", nullable = false, precision = 12, scale = 2)
    protected BigDecimal euros;

    protected Dinero() {
        this.euros = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public Dinero(BigDecimal euros) {
        if (euros == null) {
            throw new IllegalArgumentException("La cantidad de dinero no puede ser nula");
        }
        this.euros = euros.setScale(2, RoundingMode.HALF_UP);
    }

    public Dinero(double euros) {
        this(BigDecimal.valueOf(euros));
    }

    public BigDecimal getEuros() {
        return euros;
    }

    public Dinero sumar(Dinero otro) {
        if (otro == null) {
            throw new IllegalArgumentException("No se puede sumar una cantidad nula");
        }
        return new Dinero(this.euros.add(otro.euros));
    }

    public Dinero restar(Dinero otro) {
        if (otro == null) {
            throw new IllegalArgumentException("No se puede restar una cantidad nula");
        }
        return new Dinero(this.euros.subtract(otro.euros));
    }

    public boolean esMayorQue(Dinero otro) {
        if (otro == null) {
            throw new IllegalArgumentException("No se puede comparar con una cantidad nula");
        }
        return this.euros.compareTo(otro.euros) > 0;
    }

    public boolean esMenorQue(Dinero otro) {
        if (otro == null) {
            throw new IllegalArgumentException("No se puede comparar con una cantidad nula");
        }
        return this.euros.compareTo(otro.euros) < 0;
    }

    public boolean esCero() {
        return this.euros.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) == 0;
    }
}