package com.spring.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.model.Categorias;
import com.spring.web.model.Torneos;
import com.spring.web.repository.CategoriasRepository;
import com.spring.web.repository.TorneoRepository;

@Service
public class CategoriaServices {

    @Autowired
    private CategoriasRepository categoriaRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    public Categorias crearCategoria(Integer torneoId, Categorias categoria) {
        // Buscar el torneo
        Torneos torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        // Asignamos el torneo a la categoría
        categoria.setTorneos(torneo);

        // Guardamos la categoría
        Categorias categoriaGuardada = categoriaRepository.save(categoria);

       
        // Devolver la categoría creada
        return categoriaGuardada;
    }

    /**
     * Obtener una categoría por su ID
     */
    public Categorias GetCategoriasPorID(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    /**
     * Obtener todas las categorías de un torneo
     */
    public List<Categorias> obtenerCategoriasPorTorneo(Integer torneoId) {
        return categoriaRepository.findByTorneos_Id(torneoId);
    }

    /**
     * Eliminar una categoría por su ID
     */
    public void eliminarCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }
}
