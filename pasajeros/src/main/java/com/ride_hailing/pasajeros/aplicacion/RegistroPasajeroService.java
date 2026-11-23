package com.ride_hailing.pasajeros.aplicacion;

import java.util.List;
import org.springframework.stereotype.Component;

import com.ride_hailing.pasajeros.dominio.Correo;
import com.ride_hailing.pasajeros.dominio.CorreoYaRegistradoException;
import com.ride_hailing.pasajeros.dominio.Pasajero;

@Component
public class RegistroPasajeroService {

    public void validarCorreoDisponible(Correo correo, List<Pasajero> pasajerosExistentes) {
        boolean yaExiste = pasajerosExistentes.stream()
            .anyMatch(p -> p.getCorreo().equals(correo));
        if (yaExiste) {
            throw new CorreoYaRegistradoException(
                "Ya existe un pasajero registrado con el correo " + correo.valor());
        }
    }

    public void validarCambioDeCorreo(Pasajero pasajero, Correo nuevoCorreo, List<Pasajero> pasajerosExistentes) {
        boolean loTieneOtroPasajero = pasajerosExistentes.stream()
            .filter(p -> !p.getId().equals(pasajero.getId()))
            .anyMatch(p -> p.getCorreo().equals(nuevoCorreo));
        if (loTieneOtroPasajero) {
            throw new CorreoYaRegistradoException(
                "Ya existe un pasajero registrado con el correo " + nuevoCorreo.valor());
        }
        pasajero.cambiarCorreo(nuevoCorreo);
    }
}
