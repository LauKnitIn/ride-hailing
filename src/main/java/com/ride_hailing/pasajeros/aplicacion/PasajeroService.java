package com.ride_hailing.pasajeros.aplicacion;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ride_hailing.pasajeros.dominio.Correo;
import com.ride_hailing.pasajeros.dominio.Pasajero;
import com.ride_hailing.pasajeros.dominio.PasajeroNoEncontradoException;
import com.ride_hailing.pasajeros.dominio.Telefono;

@Service 
public class PasajeroService implements PasajeroUseCase {

    private final RepositorioPasajeros pasajeroRepository;
    private final PasajeroFactory pasajeroFactory;
    private final RegistroPasajeroService registroPasajeroService;

    public PasajeroService(RepositorioPasajeros pasajeroRepository,
                            PasajeroFactory pasajeroFactory,
                            RegistroPasajeroService registroPasajeroService) {
        this.pasajeroRepository = pasajeroRepository;
        this.pasajeroFactory = pasajeroFactory;
        this.registroPasajeroService = registroPasajeroService;
    }

    @Override
    public Pasajero registrar(String nombre, String correo, String telefono) {
        List<Pasajero> existentes = pasajeroRepository.listarTodos();
        Pasajero pasajero = pasajeroFactory.registrar(nombre, correo, telefono, existentes);
        pasajeroRepository.guardar(pasajero);
        return pasajero;
    }

    @Override
    public void actualizarPerfil(UUID pasajeroId, String nuevoNombre, String nuevoTelefono) {
        Pasajero pasajero = obtenerOFallar(pasajeroId);
        pasajero.actualizarPerfil(nuevoNombre, new Telefono(nuevoTelefono));
        pasajeroRepository.guardar(pasajero);
    }

    @Override
    public void cambiarCorreo(UUID pasajeroId, String nuevoCorreo) {
        Pasajero pasajero = obtenerOFallar(pasajeroId);
        List<Pasajero> existentes = pasajeroRepository.listarTodos();
        registroPasajeroService.validarCambioDeCorreo(pasajero, new Correo(nuevoCorreo), existentes);
        pasajeroRepository.guardar(pasajero);
    }

    @Override
    public Pasajero consultar(UUID pasajeroId) {
        return obtenerOFallar(pasajeroId);
    }

    private Pasajero obtenerOFallar(UUID pasajeroId) {
        return pasajeroRepository.buscarPorId(pasajeroId)
            .orElseThrow(() -> new PasajeroNoEncontradoException(pasajeroId));
    }
}
