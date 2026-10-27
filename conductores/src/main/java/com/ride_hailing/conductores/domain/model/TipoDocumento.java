package com.ride_hailing.conductores.domain.model;

public enum TipoDocumento {
    CEDULA_CIUDADANIA("CC"),
    CEDULA_EXTRANJERIA("CE"),
    PASAPORTE("PA");

    private final String sigla;

    TipoDocumento(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }

    public static TipoDocumento desde(String valor) {
        String normalizado = valor.trim().toUpperCase();
        for (TipoDocumento tipo : values()) {
            if (tipo.name().equals(normalizado) || tipo.sigla.equals(normalizado)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de documento desconocido: " + valor);
    }
}
