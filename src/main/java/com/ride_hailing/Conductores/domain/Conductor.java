package com.ride_hailing.Conductores.domain;

import java.util.Date;

public class Conductor { 
    private String idConductor;
    private String documentoIdentidad;
    private String nombreCompleto;
    private Date fechaNacimiento;
    private boolean disponibilidad;
    private String ubicacionActual;


    public Conductor(String documentoIdentidad, String nombreCompleto, Date fechaNacimiento, String ubicacionActual){
        this.documentoIdentidad = documentoIdentidad;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.ubicacionActual = ubicacionActual;
    }


    public String getIdConductor() {
        return idConductor;
    }


    public void setIdConductor(String idConductor) {
        this.idConductor = idConductor;
    }


    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }


    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }


    public String getNombreCompleto() {
        return nombreCompleto;
    }


    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }


    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }


    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    public boolean isDisponibilidad() {
        return disponibilidad;
    }


    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }


    public String getUbicacionActual() {
        return ubicacionActual;
    }


    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    
    
}
