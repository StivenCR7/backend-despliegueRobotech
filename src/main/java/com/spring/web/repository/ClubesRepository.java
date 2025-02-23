package com.spring.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.web.model.Clubes;

public interface ClubesRepository extends JpaRepository<Clubes, Integer> {

	Clubes findByCorreo(String correo);
}
