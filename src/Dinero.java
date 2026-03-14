import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dinero {

    @Column(name = "euros", nullable = false, precision = 12, scale = 2)
    protected BigDecimal euros;

    @Builder
    public Dinero(BigDecimal euros) {
        if (euros == null) {
            throw new IllegalArgumentException("La cantidad no puede ser nula");
        }
        this.euros = euros.setScale(2, RoundingMode.HALF_UP);
    }

    public Dinero sumar(Dinero otro) {
        if (otro == null) {
            throw new IllegalArgumentException("La cantidad a sumar no puede ser nula");
        }
        return Dinero.builder()
                .euros(this.euros.add(otro.euros))
                .build();
    }
}