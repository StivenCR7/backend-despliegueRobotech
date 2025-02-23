package com.spring.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.spring.web.model.VistaEncuentroRobot;

import com.spring.web.services.VistaEncuentrosRobotsService;

@RestController
@RequestMapping("vista-encuentros-robots")
public class VistaEncuentrosRobotsController {

	 @Autowired
	    private VistaEncuentrosRobotsService encuentroRobotService;

	    @GetMapping("/encuentros/categoria/{categoriaId}")
	    public List<VistaEncuentroRobot> obtenerEncuentrosPorCategoria(@PathVariable Integer categoriaId) {
	        return encuentroRobotService.obtenerEncuentrosPorCategoria(categoriaId);
	    }
}
