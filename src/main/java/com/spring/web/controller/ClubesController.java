package com.spring.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.web.model.Clubes;
import com.spring.web.model.Estados;
import com.spring.web.repository.EstadosRepository;
import com.spring.web.services.ClubServicesImpl;

@RestController
@RequestMapping("clubes")
@CrossOrigin
public class ClubesController {

	@Autowired
	private ClubServicesImpl clubServImpl;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private EstadosRepository estadoRe;

	@GetMapping
	public List<Clubes> ListarClubes() {
		return clubServImpl.ObtenerAllClubes();
	}

	@GetMapping("/listar/{id}")
	public ResponseEntity<Clubes> obtenerPorId(@PathVariable Integer id) {
		Clubes club = clubServImpl.ObtenerPorIdClubes(id);
		if (club == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(club);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<?> AgregarClub(@RequestBody Clubes clubes) {
	    // Validar que el correo no se repita
	    if ( clubes.getCorreo() == null || clubes.getCorreo().isEmpty()) {
	        return ResponseEntity.badRequest().body("Correo Electronico ya esta en uso!");
	    }

	    Clubes club = new Clubes();
	    club.setNombre(clubes.getNombre());
	    club.setTelefono(clubes.getTelefono());
	    club.setNombre_representante(clubes.getNombre_representante());
	    club.setDireccion(clubes.getDireccion());
	    club.setCorreo(clubes.getCorreo());

	    // Codificar la contraseña
	    club.setContrasena(encoder.encode(clubes.getContrasena()));

	    // Asignar el estado predeterminado
	    Estados estadoPordefecto = estadoRe.findByNombre("pendiente");
	    if (estadoPordefecto == null) {
	        return ResponseEntity.badRequest().body("{\"error\": \"Estado 'pendiente' no encontrado.\"}");
	    }
	    club.setEstados(estadoPordefecto);

	    // Guardar el club
	    clubServImpl.saveClubes(club);

	    return ResponseEntity.ok().body("{\"message\": \"Club registrado correctamente.\"}");
	}
	
	@PutMapping("/estado/{id}")
	public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestBody String nuevoEstado) {
	    // Buscar el club por ID
	    Clubes club = clubServImpl.ObtenerPorIdClubes(id);
	    if (club == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // Buscar el estado por nombre
	    Estados estado = estadoRe.findByNombre(nuevoEstado);
	    if (estado == null) {
	        return ResponseEntity.badRequest().body("{\"error\": \"Estado '" + nuevoEstado + "' no encontrado.\"}");
	    }

	    // Actualizar el estado del club
	    club.setEstados(estado);
	    clubServImpl.saveClubes(club);

	    return ResponseEntity.ok().body("{\"message\": \"Estado actualizado correctamente.\"}");
	}


	@GetMapping("/estados")
	public List<Estados> listarEstados() {
	    return estadoRe.findAll();
	}

	
	@DeleteMapping("/eliminar")
	public ResponseEntity<Void> eliminarClub(@RequestBody List<Integer> ids) {
	  ids.forEach(id -> {
		  Clubes club = clubServImpl.ObtenerPorIdClubes(id);
	    if (club != null) {
	      clubServImpl.EliminarClub(id);
	    }
	  });
	  return ResponseEntity.noContent().build();
	}
}
