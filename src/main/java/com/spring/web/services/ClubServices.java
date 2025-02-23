package com.spring.web.services;

import java.util.List;

import com.spring.web.model.Clubes;

public interface ClubServices {
	
	List<Clubes> ObtenerAllClubes();
	Clubes saveClubes(Clubes club);
	Clubes ObtenerPorIdClubes(Integer id);
	void EliminarClub(Integer id);
	Clubes obtenerCorreoClub(String correo);
}
