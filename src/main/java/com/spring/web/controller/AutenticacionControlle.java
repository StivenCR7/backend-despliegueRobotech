package com.spring.web.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.web.model.Clubes;
import com.spring.web.model.Roles;
import com.spring.web.model.Trabajadores;
import com.spring.web.repository.RolesRepository;
import com.spring.web.security.JWTObject;
import com.spring.web.security.SecurityConfig;
import com.spring.web.services.ClubServicesImpl;
import com.spring.web.services.TrabajadorServiceImpl;
import com.spring.web.sesion.Login;
import com.spring.web.sesion.Sesion;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@CrossOrigin
public class AutenticacionControlle {

	@Autowired
	private TrabajadorServiceImpl trabjadoresService;

	@Autowired
	private ClubServicesImpl clubServImpl;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@SuppressWarnings("unused")
	@Autowired
	private RolesRepository rolesRe;

	@PostMapping("/login")
	public Sesion logear(@RequestBody Login login) {
	    // Primero intentamos buscar al usuario como trabajador
	    Trabajadores trabajador = trabjadoresService.seleccionarEmailTrabajadores(login.getCorreo());
	    if (trabajador != null) {
	        boolean passwordOk = encoder.matches(login.getContrasena(), trabajador.getContrasena());
	        if (!passwordOk) {
	            throw new RuntimeException("Contraseña de inicio de sesión no válida.");
	        }

	        // Crear sesión y objeto JWT para el trabajador
	        return crearSesionParaTrabajador(trabajador);
	    }

	    // Si no es trabajador, buscamos el club por correo
	    Clubes club = clubServImpl.obtenerCorreoClub(login.getCorreo());
	    if (club != null) {
	        // Validar que el estado del club sea "aprobado"
	        if (!"aprobado".equalsIgnoreCase(club.getEstados().getNombre())) {
	            throw new RuntimeException("El club no está aprobado para iniciar sesión.");
	        }

	        boolean passwordOk = encoder.matches(login.getContrasena(), club.getContrasena());
	        if (!passwordOk) {
	            throw new RuntimeException("Contraseña de inicio de sesión no válida.");
	        }

	        // Crear sesión y objeto JWT para el club
	        return crearSesionParaClub(club);
	    }

	    // Si no se encontró ni trabajador ni club, lanzar error
	    throw new RuntimeException("No se encontró una cuenta asociada con el correo proporcionado.");
	}
	private Sesion crearSesionParaTrabajador(Trabajadores trabajador) {
	    Sesion sesion = new Sesion();
	    sesion.setLogin(trabajador.getCorreo());

	    JWTObject jwtObject = new JWTObject();
	    jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
	    jwtObject.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30))); // 30 minutos
	    jwtObject.setSubject(trabajador.getNombre());

	    // Agregar roles del trabajador
	    List<Roles> roles = new ArrayList<>();
	    if (trabajador.getRol() != null) {
	        roles.add(trabajador.getRol());
	    }
	    jwtObject.setRoles(roles);

	    // Crear claims para el token
	    Map<String, Object> claims = new HashMap<>();
	    claims.put("userId", trabajador.getId());
	    claims.put("email", trabajador.getCorreo());
	    claims.put("authorities", roles.stream().map(Roles::getNombre).collect(Collectors.toList()));

	    // Generar token
	    String token = Jwts.builder()
	            .setClaims(claims)
	            .setSubject(trabajador.getNombre())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30))) // 30 minutos
	            .signWith(SignatureAlgorithm.HS256, SecurityConfig.KEY)
	            .compact();

	    sesion.setToken(SecurityConfig.PREFIX + " " + token);
	    return sesion;
	}

	private Sesion crearSesionParaClub(Clubes club) {
	    Sesion sesion = new Sesion();
	    sesion.setLogin(club.getCorreo());

	    JWTObject jwtObject = new JWTObject();
	    jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
	    jwtObject.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30))); // 30 minutos
	    jwtObject.setSubject(club.getNombre());

	    // Crear roles para el club
	    List<Roles> roles = new ArrayList<>();
	    // Asignar un rol por defecto o según algún criterio
	    Roles rolClub = new Roles(); 
	    rolClub.setNombre("RepresentanteClub");  // O el rol que desees asignar al club
	    roles.add(rolClub);
	    jwtObject.setRoles(roles);

	    // Crear claims para el token
	    Map<String, Object> claims = new HashMap<>();
	    claims.put("clubId", club.getId());
	    claims.put("email", club.getCorreo());
	    claims.put("estado", club.getEstados().getNombre());
	    claims.put("authorities", roles.stream().map(Roles::getNombre).collect(Collectors.toList()));

	    // Generar token
	    String token = Jwts.builder()
	            .setClaims(claims)
	            .setSubject(club.getNombre())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30))) // 30 minutos
	            .signWith(SignatureAlgorithm.HS256, SecurityConfig.KEY)
	            .compact();

	    sesion.setToken(SecurityConfig.PREFIX + " " + token);
	    return sesion;
	}


}
