package com.spring.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.model.Estados;
import com.spring.web.repository.EstadosRepository;

@Service
public class EstadoServicesImpl implements EstadoServices {
	
	@Autowired
	private EstadosRepository estadoRe;

	@Override
	public List<Estados> getAllEstados() {
		return estadoRe.findAll();
	}

	@Override
	public Estados ObtenerPorIdEstados(Integer id) {
		return estadoRe.findById(id).orElse(null);
	}

	@Override
	public Estados saveEstados(Estados estado) {
		return estadoRe.save(estado);
	}

}
