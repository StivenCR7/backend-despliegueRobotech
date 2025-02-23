package com.spring.web.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.spring.web.model.Encuentros;
import com.spring.web.repository.EncuentrosRepository;
import com.spring.web.services.EncuentrosService;

@RestController
@RequestMapping("api/encuentros")
public class EncuentroController {

	@Autowired
	private EncuentrosRepository encuentroRepository;

	@Autowired
	private EncuentrosService encuentrosService;

	@GetMapping("/encuentros/categoria/{categoriaId}")
	public ResponseEntity<List<Encuentros>> obtenerEncuentrosPorCategoria(@PathVariable Integer categoriaId) {
		List<Encuentros> encuentros = encuentroRepository.findByCategoriaId(categoriaId);
		return ResponseEntity.ok(encuentros);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Encuentros> obtenerEncuentro(@PathVariable Integer id) {
		Optional<Encuentros> encuentroOpt = encuentrosService.obtenerEncuentroPorId(id);
		if (encuentroOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(encuentroOpt.get(), HttpStatus.OK);
	}

	// Endpoint para generar los encuentros de eliminación directa
	@PostMapping("/generar/{categoriaId}")
	public ResponseEntity<String> generarEncuentros(@PathVariable Integer categoriaId) {
		try {
			encuentrosService.generarEncuentrosEliminacionDirecta(categoriaId);
			return ResponseEntity.ok("Encuentros generados correctamente.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(400).body("Error al generar los encuentros: " + e.getMessage());
		}
	}

	// Endpoint para asignar robots a los encuentros de la primera ronda
	@PostMapping("/asignar-robots/{categoriaId}")
	public ResponseEntity<String> asignarRobotsAEncuentros(@PathVariable Integer categoriaId) {
		try {
			encuentrosService.asignarRobotsAEncuentrosPrimeraRonda(categoriaId);
			return ResponseEntity.ok("Robots asignados correctamente a los encuentros.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al asignar robots: " + e.getMessage());
		}
	}

	@PutMapping("/{encuentroId}/ganador/{robotGanadorId}")
	public ResponseEntity<Void> actualizarGanador(@PathVariable Integer encuentroId,
			@PathVariable Integer robotGanadorId) {
		try {
			encuentrosService.actualizarGanador(encuentroId, robotGanadorId);
			return ResponseEntity.ok().build(); // Respuesta exitosa
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Error 400 si algo no es válido
		}
	}

	@PutMapping("/{encuentroId}/fecha")
	public ResponseEntity<Encuentros> actualizarFecha(@PathVariable Integer encuentroId,
			@RequestBody Map<String, String> request) {
		Encuentros encuentro = encuentroRepository.findById(encuentroId)
				.orElseThrow(() -> new RuntimeException("Encuentro no encontrado"));

		// Extraer y parsear la nueva fecha que llega como String
		String nuevaFechaStr = request.get("fecha"); // 'fecha' viene en formato ISO
		LocalDateTime nuevaFecha = LocalDateTime.parse(nuevaFechaStr);

		// Actualizar la fecha
		encuentro.setFecha(nuevaFecha);

		// Guardar el encuentro actualizado en la base de datos
		Encuentros actualizado = encuentroRepository.save(encuentro);

		// Devolver el encuentro actualizado
		return ResponseEntity.ok(actualizado);
	}

	@PutMapping("/{encuentroId}/resultado")
	public ResponseEntity<Encuentros> actualizarResultado(@PathVariable Integer encuentroId,
			@RequestBody Map<String, String> request) {
		Encuentros encuentro = encuentroRepository.findById(encuentroId)
				.orElseThrow(() -> new RuntimeException("Encuentro no encontrado"));

		// Extraer el nuevo resultado que llega como String
		String nuevoResultado = request.get("resultado"); // 'resultado' puede ser un String que contenga el resultado

		// Actualizar el resultado
		encuentro.setResultado(nuevoResultado);

		// Guardar el encuentro actualizado en la base de datos
		Encuentros actualizado = encuentroRepository.save(encuentro);

		// Devolver el encuentro actualizado
		return ResponseEntity.ok(actualizado);
	}

}
