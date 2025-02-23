package com.spring.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.web.model.Roles;
import com.spring.web.model.Trabajadores;
import com.spring.web.repository.RolesRepository;
import com.spring.web.repository.TrabajadorRepository;
import com.spring.web.services.TrabajadorServiceImpl;

@RestController
@RequestMapping("/trabajadores")
@CrossOrigin
public class TrabajadoresController {

	@Autowired
	private TrabajadorServiceImpl trabajadoresServImpl;

	@Autowired
	private TrabajadorRepository TrabajadoresRe;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private RolesRepository rolRe;

	@GetMapping("/listar")
	public List<Trabajadores> ObtenerTrabajadores() {
	    return trabajadoresServImpl.getAllTrabajadores()
	        .stream()
	        .filter(trabajador -> !"Administrador".equalsIgnoreCase(trabajador.getRol().getNombre()))
	        .collect(Collectors.toList());
	}


	@GetMapping("/roles")
	public ResponseEntity<?> obtenerRoles() {
	    List<Roles> roles = rolRe.findAll()
	        .stream()
	        .filter(rol -> !"Administrador".equalsIgnoreCase(rol.getNombre()))
	        .collect(Collectors.toList());
	    return ResponseEntity.ok(roles);
	}


	@PostMapping("/add")
	public ResponseEntity<?> registerTrabajador(@RequestBody Trabajadores signUpRequest) {
		if (TrabajadoresRe.findByCorreo(signUpRequest.getCorreo()) != null) {
			return ResponseEntity.badRequest().body("Correo ya esta en uso!");
		}

		// Crear nuevo trabajador
		Trabajadores tra = new Trabajadores();
		tra.setNombre(signUpRequest.getNombre());
		tra.setApellido(signUpRequest.getApellido());
		tra.setTelefono(signUpRequest.getTelefono());
		tra.setCorreo(signUpRequest.getCorreo());
		tra.setContrasena(encoder.encode(signUpRequest.getContrasena()));
		tra.setRol(signUpRequest.getRol());
		trabajadoresServImpl.saveTrabajadores(tra);
		return ResponseEntity.ok().body("trabajador creado exitosamente ");
	}

	// Actualizar datos de contacto
	@PutMapping("/update-datos/{id}")
	public ResponseEntity<?> updateContactInfo(@PathVariable Integer id, @RequestBody Map<String, String> updates) {
		// Buscar al trabajador por ID
		Trabajadores trabajador = trabajadoresServImpl.seleccionarIdTrabajadores(id);
		if (trabajador == null) {
			return ResponseEntity.badRequest().body("El trabajador con el ID especificado no existe.");
		}

		// Actualizar los campos de contacto si están presentes en la solicitud
		if (updates.containsKey("telefono")) {
			trabajador.setTelefono(updates.get("telefono"));
		}
		if (updates.containsKey("correo")) {
			String nuevoCorreo = updates.get("correo");
			// Verificar si el correo ya está en uso
			if (TrabajadoresRe.findByCorreo(nuevoCorreo) != null && !nuevoCorreo.equals(trabajador.getCorreo())) {
				return ResponseEntity.badRequest().body("El correo ya está en uso.");
			}
			trabajador.setCorreo(nuevoCorreo);
		}

		// Guardar los cambios
		trabajadoresServImpl.saveTrabajadores(trabajador);

		return ResponseEntity.ok().body("Información de contacto actualizada exitosamente.");
	}

	// Método para actualizar solo el rol del trabajador
	@PatchMapping("/updateRol/{idTrabajador}")
	public ResponseEntity<?> updateRol(@PathVariable Integer idTrabajador, @RequestBody Integer idRol) {

		// Buscar el trabajador existente
		Trabajadores trabajadorExistente = TrabajadoresRe.findById(idTrabajador).orElse(null);
		if (trabajadorExistente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trabajador no encontrado");
		}

		// Buscar el rol por su ID
		Roles nuevoRol = rolRe.findById(idRol).orElse(null);
		if (nuevoRol == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
		}

		// Actualizar solo el rol
		trabajadorExistente.setRol(nuevoRol);

		// Guardar los cambios
		TrabajadoresRe.save(trabajadorExistente);

		return ResponseEntity.ok("Rol actualizado correctamente");
	}

	@DeleteMapping("/delete/{idTrabajador}")
	public ResponseEntity<?> deleteTrabajador(@PathVariable Integer idTrabajador) {
		// Buscar el trabajador existente
		Trabajadores trabajadorExistente = TrabajadoresRe.findById(idTrabajador).orElse(null);
		if (trabajadorExistente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trabajador no encontrado");
		}

		// Eliminar el trabajador
		TrabajadoresRe.delete(trabajadorExistente);
		return ResponseEntity.ok("Trabajador eliminado correctamente");
	}
	@GetMapping("/listar/{id}")
    public ResponseEntity<?> obtenerTrabajadorPorID(@PathVariable Integer id) {
        Trabajadores trabajador = trabajadoresServImpl.seleccionarIdTrabajadores(id);
        if (trabajador == null) {
            return ResponseEntity.badRequest().body("Trabajador no encontrado.");
        }
        return ResponseEntity.ok(trabajador);
    }

}
