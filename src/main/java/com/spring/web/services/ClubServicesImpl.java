package com.spring.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.model.Clubes;
import com.spring.web.repository.ClubesRepository;

@Service
public class ClubServicesImpl implements ClubServices {
	
	@Autowired
	private ClubesRepository clubesRe;

	@Override
	public List<Clubes> ObtenerAllClubes() {
		return clubesRe.findAll();
	}

	@Override
	public Clubes saveClubes(Clubes club) {
		return clubesRe.save(club);
	}

	@Override
	public Clubes ObtenerPorIdClubes(Integer id) {
		return clubesRe.findById(id).orElse(null);
	}

	@Override
	public void EliminarClub(Integer id) {
		clubesRe.deleteById(id);
	}

	@Override
	public Clubes obtenerCorreoClub(String correo) {
		return clubesRe.findByCorreo(correo);
	}

}
