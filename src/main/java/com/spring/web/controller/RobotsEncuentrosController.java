package com.spring.web.controller;

import com.spring.web.model.RobotsEncuentros;
import com.spring.web.services.RobotsEncuentrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/api/robots-encuentros")
public class RobotsEncuentrosController {

    @Autowired
    private RobotsEncuentrosService service;

    @GetMapping("/encuentro/{encuentroId}")
    public ResponseEntity<List<RobotsEncuentros>> obtenerResultadosPorEncuentro(@PathVariable Integer encuentroId) {
        List<RobotsEncuentros> resultados = service.obtenerResultadosPorEncuentro(encuentroId);
        if (resultados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }


}

