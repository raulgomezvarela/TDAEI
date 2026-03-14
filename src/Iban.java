import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Iban {

    @Column(name = "iban", nullable = false, length = 34)
    protected String valor;

    @Builder
    public Iban(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El IBAN no puede ser nulo ni vacío");
        }
        this.valor = valor.replaceAll("\\s+", "").toUpperCase();
    }

    public void validarFormato() {
        if (!this.valor.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$")) {
            throw new IllegalArgumentException("Formato de IBAN no válido");
        }
    }
}