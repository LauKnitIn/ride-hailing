package com.ride_hailing.conductores.aplicacion;



import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.ride_hailing.conductores.application.ConductorFactory;
import com.ride_hailing.conductores.application.ConductorService;
import com.ride_hailing.conductores.application.puerto.salida.ViajeActivoPort;
import com.ride_hailing.conductores.domain.excepcion.ConductorConViajeActivoException;
import com.ride_hailing.conductores.domain.excepcion.ConductorDuplicadoException;
import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.EstadoDisponibilidad;
import com.ride_hailing.conductores.domain.model.TipoDocumento;
import com.ride_hailing.conductores.domain.model.UbicacionGeografica;
import com.ride_hailing.conductores.domain.service.ConductorDomainService;

class ConductorServiceConFalsoTest {

    private ConductorService crearServicio(ConductorRepositoryFalso repo, ViajeActivoPort viajeActivo) {
        return new ConductorService(repo, viajeActivo, new ConductorFactory(repo), new ConductorDomainService());
    }

    @Test
    void registraConductorYRegistraDisponibilidadSinSpringNiBaseDeDatos() {
        ConductorService service = crearServicio(new ConductorRepositoryFalso(), id -> false);

        Conductor registrado = service.registrar("Ana Torres", TipoDocumento.values()[0], "1234567", LocalDate.now());
        Conductor disponible = service.registrarDisponibilidad(registrado.getIdConductor(), new UbicacionGeografica(5.53, -73.36));

        assertThat(disponible.getDisponibilidad()).isEqualTo(EstadoDisponibilidad.DISPONIBLE);
        assertThat(service.buscarDisponibles()).hasSize(1);
    }

    @Test
    void noPermiteRegistrarDosVecesElMismoDocumento() {
        ConductorService service = crearServicio(new ConductorRepositoryFalso(), id -> false);
        service.registrar("Ana Torres", TipoDocumento.values()[0], "1234567", LocalDate.now());

        assertThatThrownBy(() -> service.registrar("Otra Persona", TipoDocumento.values()[0], "1234567", LocalDate.now()))
                .isInstanceOf(ConductorDuplicadoException.class);
    }

    
}
