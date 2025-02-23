package com.spring.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.web.model.Estados;
import com.spring.web.services.EstadoServicesImpl;

@RestController
@RequestMapping("estados")
@CrossOrigin
public class EstadosController {

	@Autowired
	private EstadoServicesImpl estadoServImpl;
	
	@GetMapping
	public List<Estados> listarEstados(){
		return estadoServImpl.getAllEstados();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Estados> ObtenerPorId(@PathVariable Integer id){
		Estados estado = estadoServImpl.ObtenerPorIdEstados(id);
        if (estado==null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(estado);
        }
	}
	
	@PostMapping("/agregar")
	public Estados saveEstados(@RequestBody Estados estado) {
		return estadoServImpl.saveEstados(estado);
	}
}
