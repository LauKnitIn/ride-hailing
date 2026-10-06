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

}
