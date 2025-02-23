package com.spring.web.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.web.model.Categorias;
import com.spring.web.model.Competidores;
import com.spring.web.model.Estados;
import com.spring.web.model.Robots;
import com.spring.web.repository.CategoriasRepository;
import com.spring.web.repository.EstadosRepository;
import com.spring.web.security.SecurityConfig;
import com.spring.web.services.RobotServices;
import com.spring.web.services.UploadFileService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/robots")
public class RobotsController {

	@Autowired
	private RobotServices robotService;

	@Autowired
	private EstadosRepository estadoRe;

	@Autowired
	private UploadFileService upload;

	@Autowired
	private CategoriasRepository categoriasRepository;

	// Registrar un nuevo robot
	@PostMapping("/registrar/{categoriaId}")
	public ResponseEntity<?> registrarRobot(@PathVariable Integer categoriaId, @RequestParam("nombre") String nombre,
			@RequestParam("peso") String peso, @RequestParam("competidorId") Integer competidorId,
			@RequestParam("dimensiones") String dimensiones, @RequestParam("foto") MultipartFile file) {
		try {
			Robots robot = new Robots();
			robot.setNombre(nombre);
			robot.setPeso(peso);
			robot.setDimensiones(dimensiones);

			// Procesar la imagen
			if (!file.isEmpty()) {
				String nombreImagen = upload.saveImage(file);
				robot.setFoto(nombreImagen);
			} else {
				robot.setFoto("default.jpg");
			}

			// Asignar competidor
			Competidores competidor = new Competidores();
			competidor.setId(competidorId);
			robot.setCompetidores(competidor);

			// Registrar el robot
			Robots robotRegistrado = robotService.registrarRobotCategoria(categoriaId, robot);

			return ResponseEntity.ok(robotRegistrado);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Error al procesar la imagen.\"}");
		}
	}

	@PutMapping("/estado/{id}")
	public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestBody String nuevoEstado) {
		// Buscar el club por ID
		Robots robot = robotService.obtenerRobotID(id);
		if (robot == null) {
			return ResponseEntity.notFound().build();
		}

		// Buscar el estado por nombre
		Estados estado = estadoRe.findByNombre(nuevoEstado);
		if (estado == null) {
			return ResponseEntity.badRequest().body("{\"error\": \"Estado '" + nuevoEstado + "' no encontrado.\"}");
		}

		// Actualizar el estado del club
		robot.setEstados(estado);
		robotService.saveRobot(robot);

		return ResponseEntity.ok().body("{\"message\": \"Estado actualizado correctamente.\"}");
	}
	
	@GetMapping("/listarPorClub")
	public ResponseEntity<?> listarRobotsPorClub(@RequestHeader("Authorization") String token) {
	    // Obtener el ID del club desde el token JWT
		
	    String jwt = token.replace("Bearer ", "");
	    Claims claims;
	    try {
	        claims = Jwts.parser()
	                .setSigningKey(SecurityConfig.KEY)
	                .parseClaimsJws(jwt)
	                .getBody();
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Token inválido o expirado.\"}");
	    }

	    Integer clubId = claims.get("clubId", Integer.class);
	    if (clubId == null) {
	        return ResponseEntity.badRequest().body("{\"error\": \"No se encontró el ID del club en el token.\"}");
	    }

	    List<Robots> robots = robotService.listarRobotsPorClub(clubId);
	    return ResponseEntity.ok(robots);
	}


	@GetMapping("/estados")
	public List<Estados> listarEstados() {
		return estadoRe.findAll();
	}

	// Obtenemos los robots por categoria
	@GetMapping("/listar/{categoriaId}")
	public List<Robots> obtenerRobotsPorCategoria(@PathVariable Integer categoriaId) {
		return robotService.obtenerRobotsPorCategoria(categoriaId);
	}

	@GetMapping("/listarPorCompetidor/{competidorId}")
	public ResponseEntity<List<Robots>> listarRobotsPorCompetidor(@PathVariable Integer competidorId) {
	    List<Robots> robots = robotService.listarRobotsPorCompetidor(competidorId);
	    return ResponseEntity.ok(robots);
	}
	
	@GetMapping("/obtener/categorias")
	public List<Categorias> listarCategorias() {
		return categoriasRepository.findAll();
	}

	
	// Eliminar robots
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> eliminarRobot(@PathVariable Integer id) {
		robotService.eliminarRobot(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
} 

