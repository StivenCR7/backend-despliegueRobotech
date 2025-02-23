package com.spring.web.controller;

import com.spring.web.model.Categorias;
import com.spring.web.services.CategoriaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaServices categoriaServices;

    @GetMapping("/{torneoId}")
    public ResponseEntity<List<Categorias>> obtenerCategorias(@PathVariable Integer torneoId) {
        List<Categorias> categorias = categoriaServices.obtenerCategoriasPorTorneo(torneoId);

        if (categorias == null || categorias.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Categorías no encontradas
        }

        return ResponseEntity.ok(categorias);
    }
}
