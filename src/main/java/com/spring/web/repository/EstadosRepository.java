package com.spring.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.web.model.Estados;

public interface EstadosRepository extends JpaRepository<Estados, Integer> {
	
	Estados findByNombre(String nombre);
}
