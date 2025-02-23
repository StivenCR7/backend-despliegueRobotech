package com.spring.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.model.Competidores;
import com.spring.web.repository.CompetidorRepository;

@Service
public class CompetidorServices {

	@Autowired
	private CompetidorRepository competidorRepository;
	

	public Competidores agregarCompetidor(Competidores competidor) {
		return competidorRepository.save(competidor);
	}

	public List<Competidores> obtenerTodosLosCompetidores() {
		return competidorRepository.findAll();
	}

	public Competidores obtenerCompetidorPorId(Integer id) {
		return competidorRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("competidor no encontrado"));
	}

	public void eliminarCompetidor(Integer id) {
		competidorRepository.deleteById(id);
	}
	
	public Competidores buscarPorDni(String dni) {
        return competidorRepository.findByDni(dni);
    }
}

