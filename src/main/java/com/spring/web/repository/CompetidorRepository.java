package com.spring.web.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.web.model.Competidores;

public interface CompetidorRepository extends JpaRepository<Competidores, Integer> {
	Competidores findByDni(String dni);
}
