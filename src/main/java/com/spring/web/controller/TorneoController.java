package com.spring.web.controller;

import com.spring.web.model.Categorias;
import com.spring.web.model.Torneos;
import com.spring.web.services.CategoriaServices;
import com.spring.web.services.TorneoServices;
import com.spring.web.services.UploadFileService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/torneos")
public class TorneoController {

	@Autowired
	private UploadFileService img;
	
	@Autowired
    private CategoriaServices categoriaService;
	
	@Autowired
    private TorneoServices torneoService;

    @PostMapping("/crear")
    public ResponseEntity<Torneos> crearTorneo(@RequestBody Torneos torneo) {
        return new ResponseEntity<>(torneoService.crearTorneo(torneo), HttpStatus.CREATED);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Torneos> editarTorneo(@PathVariable Integer id, @RequestBody Torneos torneo) {
        Torneos torneoActualizado = torneoService.editarTorneo(id, torneo);
        return new ResponseEntity<>(torneoActualizado, HttpStatus.OK);
    }

    @GetMapping
    public List<Torneos> obtenerTodosLosTorneos() {
        return torneoService.obtenerTodosLosTorneos();
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<Torneos> obtenerTorneoPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(torneoService.obtenerTorneoPorId(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarTorneo(@PathVariable Integer id) {
        torneoService.eliminarTorneo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping("/{torneoId}/categorias")
    public ResponseEntity<Categorias> crearCategoria(
            @PathVariable Integer torneoId, 
            @RequestParam("nombre") String nombre,
            @RequestParam("formato") String formato,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("banner") MultipartFile file) {

        try {
            // Crear una nueva instancia de Categorias
            Categorias categoria = new Categorias();
            
            // Guardar la imagen y obtener el nombre o la ruta
            String nombreBanner = img.saveImage(file); // Asumiendo que tienes un método saveImage en tu clase img
            categoria.setBanner(nombreBanner);
            
            // Establecer otros atributos
            categoria.setNombre(nombre);
            categoria.setFormato(formato);
            categoria.setCantidad(cantidad);
            
            // Llamar al servicio para guardar la categoría
            Categorias nuevaCategoria = categoriaService.crearCategoria(torneoId, categoria);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{torneoId}/todasCate")
    public List<Categorias> obtenerCategoriasPorTorneo(@PathVariable Integer torneoId) {
        return categoriaService.obtenerCategoriasPorTorneo(torneoId);
    }

    @DeleteMapping("/deleteCate/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
