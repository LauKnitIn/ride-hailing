package com.ride_hailing.conductores.domain.model;

public class DocumentoIdentidad {
    
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;

    public DocumentoIdentidad(String numeroDocumento, TipoDocumento tipoDocumento) {
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento(){
        return this.numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento){
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento(){
        return this.tipoDocumento.toString();
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento){
        this.tipoDocumento = tipoDocumento;
    }

    public String getNombre(){
        return this.tipoDocumento.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DocumentoIdentidad otro
                && java.util.Objects.equals(numeroDocumento, otro.numeroDocumento)
                && tipoDocumento == otro.tipoDocumento;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(numeroDocumento, tipoDocumento);
    }

}
