package com.ride_hailing.Conductores.domain;

import java.time.LocalDate;

public class Conductor { 
    private String idConductor;
    private String documentoIdentidad;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private boolean disponibilidad;
    private String ubicacionActual;


    public Conductor(
        String idConductor,
        String documentoIdentidad,
        String nombreCompleto, 
        LocalDate fechaNacimiento, 
        String ubicacionActual,
        boolean disponibilidad
    ){
        this.disponibilidad = disponibilidad;
        this.idConductor = idConductor;
        this.documentoIdentidad = documentoIdentidad;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.ubicacionActual = ubicacionActual;
    }


    public String getIdConductor() {
        return idConductor;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void registrarDisponibilidad (boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void desactivarDisponibilidad() {
        this.disponibilidad = false;
    }


    public void actualizarUbicacion (String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public String conocerUbicacionActual(){
        return this.ubicacionActual;
    }
    
    
}
