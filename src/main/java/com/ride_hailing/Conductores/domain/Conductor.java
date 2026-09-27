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
    
}
