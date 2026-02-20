package com.kivora.JsWater.domain.valueobject.Adress;

import java.util.Objects;

public class Address {

    private final String bairro;
    private final String localidade;
    private final String rua;
    private final String referencia;

    public Address(String bairro, String localidade, String rua, String referencia) {

        if (bairro == null || localidade == null || rua == null ) {
            throw new IllegalArgumentException("Bairro or localidade or rua is null");
        }

        this.bairro = bairro;
        this.localidade = localidade;
        this.rua = rua;
        this.referencia = referencia;
    }

    public static Address empty() {
        return new Address("", "", "", ""); // todos os campos vazios
    }

    public String getBairro() {
        return bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getRua() {
        return rua;
    }

    public String getReferencia() {
        return referencia;
    }

    public String enderecoCompleto() {
        return bairro + ", " + localidade + ", " + rua + ", " + referencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address that)) return false;
        return Objects.equals(bairro, that.bairro)
                && Objects.equals(localidade, that.localidade)
                && Objects.equals(rua, that.rua)
                && Objects.equals(referencia, that.referencia);

    }

    @Override
    public int hashCode() {
        return Objects.hash(bairro, localidade, rua, referencia);
    }
}
