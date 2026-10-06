package com.ride_hailing.pasajeros.dominio;

import java.util.Objects;
import java.util.UUID;

public class Pasajero {

    private final UUID id;
    private String nombre;
    private Correo correo;
    private Telefono telefono;

    public Pasajero(UUID id, String nombre, Correo correo, Telefono telefono) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public void actualizarPerfil(String nuevoNombre, Telefono nuevoTelefono) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        this.nombre = nuevoNombre.trim();
        this.telefono = Objects.requireNonNull(nuevoTelefono, "el teléfono es obligatorio");
    }

    public void cambiarCorreo(Correo nuevoCorreo) {
        this.correo = Objects.requireNonNull(nuevoCorreo, "el correo es obligatorio");
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Correo getCorreo() {
        return correo;
    }

    public Telefono getTelefono() {
        return telefono;
    }

}
