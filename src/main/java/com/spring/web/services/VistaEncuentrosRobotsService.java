package com.spring.web.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.model.VistaEncuentroRobot;

import com.spring.web.repository.VistaEncuentroRobotRepository;

@Service
public class VistaEncuentrosRobotsService {

	 @Autowired
	    private VistaEncuentroRobotRepository encuentroRobotRepository;

	    public List<VistaEncuentroRobot> obtenerEncuentrosPorCategoria(Integer categoriaId) {
	        return encuentroRobotRepository.findByCategoriaId(categoriaId);
	    }
}
