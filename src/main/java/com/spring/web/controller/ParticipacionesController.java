package com.spring.web.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.web.model.Participaciones;
import com.spring.web.services.ParticipacionesService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/participaciones")
public class ParticipacionesController {

    @Autowired
    private ParticipacionesService participacionesService;

    // Crear una nueva participación
    @PostMapping
    public ResponseEntity<Participaciones> crearParticipacion(@RequestBody Participaciones participacion) {
        Participaciones nuevaParticipacion = participacionesService.crearParticipacion(participacion);
        return new ResponseEntity<>(nuevaParticipacion, HttpStatus.CREATED);
    }

    // Obtener todas las participaciones
    @GetMapping
    public List<Participaciones> obtenerTodasLasParticipaciones() {
        return participacionesService.obtenerTodasLasParticipaciones();
    }

    // Obtener una participación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Participaciones> obtenerParticipacionPorId(@PathVariable Integer id) {
        try {
            Participaciones participacion = participacionesService.obtenerParticipacionPorId(id)
                    .orElseThrow(() -> new NoSuchElementException("Participacion no encontrada"));
            return new ResponseEntity<>(participacion, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar una participación
    @PutMapping("/{id}")
    public ResponseEntity<Participaciones> actualizarParticipacion(@PathVariable Integer id, @RequestBody Participaciones participacion) {
        Participaciones participacionActualizada = participacionesService.actualizarParticipacion(id, participacion);
        return new ResponseEntity<>(participacionActualizada, HttpStatus.OK);
    }

    // Eliminar una participación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarParticipacion(@PathVariable Integer id) {
        participacionesService.eliminarParticipacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
