package com.spring.web.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.web.model.VistaEncuentroRobot;

import java.util.List;

public interface VistaEncuentroRobotRepository extends JpaRepository<VistaEncuentroRobot, Integer> {

    List<VistaEncuentroRobot> findByCategoriaId(Integer categoriaId);
}