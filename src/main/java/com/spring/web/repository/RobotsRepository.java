package com.spring.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.web.model.Robots;

public interface RobotsRepository extends JpaRepository<Robots, Integer>{
	List<Robots> findByCategoriasIdAndEstadosId(Integer categoriaId, Integer estadoId);
	List<Robots> findByCompetidores_Clubes_Id(Integer clubId);
	List<Robots> findByCompetidores_Id(Integer competidorId);
	List<Robots> findByCategorias_Id(Integer categoriaId);
}
