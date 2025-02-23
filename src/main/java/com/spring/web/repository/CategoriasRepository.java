package com.spring.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Categorias;
@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, Integer>{
	
	List<Categorias> findByTorneos_Id(Integer torneoId);
	
}
