package com.spring.web.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import com.spring.web.model.Participaciones;
import com.spring.web.repository.ParticipacionesRepository;

@Service
public class ParticipacionesServiceImpl implements ParticipacionesService {

    @Autowired
    private ParticipacionesRepository participacionesRepository;

    @Override
    public Participaciones crearParticipacion(Participaciones participacion) {
        return participacionesRepository.save(participacion);
    }

    @Override
    public List<Participaciones> obtenerTodasLasParticipaciones() {
        return participacionesRepository.findAll();
    }

    @Override
    public Optional<Participaciones> obtenerParticipacionPorId(Integer id) {
        return participacionesRepository.findById(id);
    }

    @Override
    public Participaciones actualizarParticipacion(Integer id, Participaciones participacionActualizada) {
        return participacionesRepository.findById(id)
                .map(participacion -> {
                    participacion.setRobot(participacionActualizada.getRobot());
                    participacion.setCategoria(participacionActualizada.getCategoria());
                    participacion.setVictorias(participacionActualizada.getVictorias());
                    participacion.setDerrotas(participacionActualizada.getDerrotas());
                    participacion.setPosicionFinal(participacionActualizada.getPosicionFinal());
                    return participacionesRepository.save(participacion);
                })
                .orElseThrow(() -> new NoSuchElementException("Participacion no encontrada"));
    }

    @Override
    public void eliminarParticipacion(Integer id) {
        participacionesRepository.deleteById(id);
    }
}
