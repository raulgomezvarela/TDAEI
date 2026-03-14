import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Iban {

    @Column(name = "iban", nullable = false, unique = true, length = 34)
    protected String valor;

    protected Iban() {
    }

    public Iban(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El IBAN no puede ser nulo ni vacío");
        }

        String ibanNormalizado = valor.replaceAll("\\s+", "").toUpperCase();

        if (!esFormatoValido(ibanNormalizado)) {
            throw new IllegalArgumentException("El formato del IBAN no es válido");
        }

        this.valor = ibanNormalizado;
    }

    public String getValor() {
        return valor;
    }

    public void validarFormato() {
        if (!esFormatoValido(this.valor)) {
            throw new IllegalStateException("El IBAN almacenado no es válido");
        }
    }

    private boolean esFormatoValido(String iban) {
        return iban.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$");
    }

}
