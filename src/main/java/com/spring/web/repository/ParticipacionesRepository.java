package com.spring.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Participaciones;

@Repository
public interface ParticipacionesRepository extends JpaRepository<Participaciones, Integer> {
}
