package com.spring.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.web.model.Torneos;

public interface TorneoRepository extends JpaRepository<Torneos, Integer>{

}
