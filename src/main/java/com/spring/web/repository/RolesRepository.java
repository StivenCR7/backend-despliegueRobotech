package com.spring.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.web.model.Roles;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
	Roles findByNombre(String nombre);

}
