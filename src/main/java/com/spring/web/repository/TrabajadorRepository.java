package com.spring.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.web.model.Trabajadores;

public interface TrabajadorRepository extends JpaRepository<Trabajadores, Integer>{
	Trabajadores findByCorreo(String correo);

}
