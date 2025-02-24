 package com.spring.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.web.model.Clubes;
import com.spring.web.model.Competidores;
import com.spring.web.model.Estados;
import com.spring.web.repository.ClubesRepository;
import com.spring.web.repository.CompetidorRepository;
import com.spring.web.repository.EstadosRepository;
import com.spring.web.security.SecurityConfig;
import com.spring.web.services.CompetidorServices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/competidores")
public class CompetidorController {

	@Autowired
	private CompetidorServices competidorService;

	@Autowired
	private EstadosRepository estadosRepository;

	@Autowired
	private ClubesRepository clubesRepository;
	
	@Autowired
	private CompetidorRepository competidoresRepository;

	@GetMapping("/listar")
	public List<Competidores> listarCompetidores() {
		return competidorService.obtenerTodosLosCompetidores();
	}


	@PostMapping("/add")
	public ResponseEntity<?> CrearCompetidores(
	        @RequestBody Competidores competidores,
	        @RequestHeader("Authorization") String token) {

	    // Eliminar el prefijo "Bearer " del token
	    String jwt = token.replace("Bearer ", "");

	    // Decodificar el token y obtener los claims
	    Claims claims;
	    try {
	        claims = Jwts.parser()
	                .setSigningKey(SecurityConfig.KEY)
	                .parseClaimsJws(jwt)
	                .getBody();
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Token inválido o expirado.\"}");
	    }

	    // Extraer el clubId del token (ya es Integer, no es necesario parsearlo)
	    Integer clubId = claims.get("clubId", Integer.class);
	    if (clubId == null) {
	        return ResponseEntity.badRequest().body("{\"error\": \"No se encontró el ID del club en el token.\"}");
	    }

	    // Validar que el ID del club exista en la base de datos
	    Clubes club = clubesRepository.findById(clubId).orElse(null);
	    if (club == null) {
	        return ResponseEntity.badRequest().body("{\"error\": \"El club especificado no existe.\"}");
	    }

	    // Validar que el correo no esté vacío
	    if (competidores.getCorreo() == null || competidores.getCorreo().isEmpty()) {
	        return ResponseEntity.badRequest().body("{\"error\": \"Correo Electrónico no puede estar vacío!\"}");
	    }
	    
	    // Validar que el alias sea único
	    // Suponiendo que tienes un método en el repositorio: findByAlias(String alias)
	    Competidores aliasExistente = competidoresRepository.findByAlias(competidores.getAlias());
	    if (aliasExistente != null) {
	        return ResponseEntity.badRequest().body("{\"error\": \"El alias ya está en uso. Por favor, elige otro.\"}");
	    }

	    // Asignar el estado predeterminado
	    Estados estadoPordefecto = estadosRepository.findByNombre("pendiente");
	    if (estadoPordefecto == null) {
	        return ResponseEntity.badRequest().body("{\"error\": \"Estado 'pendiente' no encontrado.\"}");
	    }

	    // Crear el objeto competidor con los valores correctos
	    Competidores nuevoCompetidor = new Competidores();
	    nuevoCompetidor.setNombre(competidores.getNombre());
	    nuevoCompetidor.setApellido(competidores.getApellido());
	    nuevoCompetidor.setAlias(competidores.getAlias());
	    nuevoCompetidor.setDni(competidores.getDni());
	    nuevoCompetidor.setEdad(competidores.getEdad());
	    nuevoCompetidor.setCorreo(competidores.getCorreo());
	    nuevoCompetidor.setClubes(club); // Asociar el club extraído del token
	    nuevoCompetidor.setEstados(estadoPordefecto); // Asignar el estado predeterminado

	    // Guardar el nuevo competidor
	    competidorService.agregarCompetidor(nuevoCompetidor);

	    return ResponseEntity.ok().body("{\"message\": \"Competidor registrado correctamente.\"}");
	}


	@PutMapping("/estado/{id}")
	public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestBody String nuevoEstado) {
		// Buscar el club por ID
		Competidores competidor = competidorService.obtenerCompetidorPorId(id);
		if (competidor == null) {
			return ResponseEntity.notFound().build();
		}

		// Buscar el estado por nombre
		Estados estado = estadosRepository.findByNombre(nuevoEstado);
		if (estado == null) {
			return ResponseEntity.badRequest().body("{\"error\": \"Estado '" + nuevoEstado + "' no encontrado.\"}");
		}

		// Actualizar el estado del club
		competidor.setEstados(estado);
		competidorService.agregarCompetidor(competidor);

		return ResponseEntity.ok().body("{\"message\": \"Estado actualizado correctamente.\"}");
	}

	@GetMapping("/estados")
	public List<Estados> listarEstados() {
		return estadosRepository.findAll();
	}

}
