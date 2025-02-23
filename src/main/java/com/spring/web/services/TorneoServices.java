package com.spring.web.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.web.repository.TorneoRepository;
import com.spring.web.model.Torneos;

@Service
public class TorneoServices {

	@Autowired
	private TorneoRepository torneoRepository;

	

	public Torneos crearTorneo(Torneos torneo) {
	    validarFechas(torneo);
	    Torneos torneoCreado = torneoRepository.save(torneo);

	    return torneoCreado;
	}
	public Torneos editarTorneo(Integer id, Torneos torneoActualizado) {
		Torneos torneoExistente = torneoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Torneo no encontrado con ID: " + id));
		
		if (torneoExistente.getFecha_fin().isBefore(LocalDate.now())) {
			throw new RuntimeException("No se puede modificar un torneo cuya fecha de fin ya ha pasado.");
		}

		validarFechas(torneoActualizado);
		torneoExistente.setNombre(torneoActualizado.getNombre());
		torneoExistente.setFecha_inicio(torneoActualizado.getFecha_inicio());
		torneoExistente.setFecha_fin(torneoActualizado.getFecha_fin());

		
		return torneoRepository.save(torneoExistente);
	}

	public List<Torneos> obtenerTodosLosTorneos() {
		return torneoRepository.findAll();
	}

	public Torneos obtenerTorneoPorId(Integer id) {
		return torneoRepository.findById(id).orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
	}

	public void eliminarTorneo(Integer id) {
		torneoRepository.deleteById(id);
	}

	private void validarFechas(Torneos torneo) {
		if (torneo.getFecha_inicio().isBefore(LocalDate.now())) {
			throw new RuntimeException("La fecha de inicio no puede ser anterior a la fecha actual.");
		}
		if (torneo.getFecha_inicio().isAfter(torneo.getFecha_fin())) {
			throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha de fin.");
		}
	}
	

   
}
