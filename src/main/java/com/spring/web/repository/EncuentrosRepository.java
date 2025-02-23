package com.spring.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Encuentros;

@Repository
public interface EncuentrosRepository extends JpaRepository<Encuentros, Integer> {
	 // Método para encontrar los encuentros por categoría y ronda
    List<Encuentros> findByCategoriaIdAndRonda(Integer categoriaId, Integer ronda);
    List<Encuentros> findByCategoriaId(Integer categoriaId);
}
