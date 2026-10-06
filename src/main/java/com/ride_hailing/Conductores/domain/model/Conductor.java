package com.ride_hailing.Conductores.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Conductor { 
    private final DriverId idConductor;
    private final DocumentoIdentidad documentoIdentidad;
    private String nombreCompleto;
    private final LocalDate fechaNacimiento;
    private EstadoDisponibilidad disponibilidad;
    private UbicacionGeografica ubicacionActual;


    public Conductor(
        DriverId idConductor,
        DocumentoIdentidad documentoIdentidad,
        String nombreCompleto, 
        LocalDate fechaNacimiento
    ){
        this.disponibilidad = EstadoDisponibilidad.INACTIVO;
        this.idConductor = Objects.requireNonNull(idConductor, "El ID del conductor es obligatorio.");
        this.documentoIdentidad = Objects.requireNonNull(documentoIdentidad, "El documento de identidad es obligatorio.");
        validarNombre(nombreCompleto);
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = Objects.requireNonNull(fechaNacimiento, "La fecha de nacimiento es obligatoria.");
        validarMayoriaEdad(fechaNacimiento);
        this.ubicacionActual = null;
    }

    private void validarNombre(String nombre){
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre completo no puede estar vacío.");
        }
    }

    private void validarMayoriaEdad(LocalDate fechaNacimiento){
        if(fechaNacimiento == null){
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 18){
            throw new IllegalArgumentException("El conductor debe ser mayor de edad.");
        }
    }

    public void registrarDisponibilidad () {
        if(this.ubicacionActual == null){
            throw new IllegalStateException("No se puede registrar disponibilidad sin una ubicación actual.");
        }
        if(this.disponibilidad == EstadoDisponibilidad.EN_VIAJE){
            throw new IllegalStateException("No se puede registrar disponibilidad mientras el conductor está en viaje.");
        }
        this.disponibilidad = EstadoDisponibilidad.DISPONIBLE;
    }

    public void desactivarDisponibilidad() {
        if (this.disponibilidad == EstadoDisponibilidad.EN_VIAJE) {
            throw new IllegalStateException("No es posible desactivar la disponibilidad durante un viaje activo.");
        }
        this.disponibilidad = EstadoDisponibilidad.INACTIVO;
    }

    public void actualizarUbicacion (UbicacionGeografica ubicacionActual) {
        this.ubicacionActual = Objects.requireNonNull(ubicacionActual, "La ubicación actual no puede ser nula.");
    }

    public DriverId getIdConductor() {
        return idConductor;
    }

    public DocumentoIdentidad getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public EstadoDisponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public UbicacionGeografica getUbicacionActual() {
        return ubicacionActual;
    }
    
}
