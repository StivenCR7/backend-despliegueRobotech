package com.spring.web.services;

import java.util.List;
import java.util.Optional;

import com.spring.web.model.Participaciones;

public interface ParticipacionesService {

    // Crear una nueva participación
    Participaciones crearParticipacion(Participaciones participacion);

    // Obtener todas las participaciones
    List<Participaciones> obtenerTodasLasParticipaciones();

    // Obtener una participación por ID
    Optional<Participaciones> obtenerParticipacionPorId(Integer id);

    // Actualizar una participación
    Participaciones actualizarParticipacion(Integer id, Participaciones participacionActualizada);

    // Eliminar una participación
    void eliminarParticipacion(Integer id);
}

