package com.spring.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.model.Trabajadores;
import com.spring.web.repository.TrabajadorRepository;

@Service
public class TrabajadorServiceImpl implements TrabajadorServices{

	@Autowired
	private TrabajadorRepository trabajadorRe;
	
	
	
	@Override
	public List<Trabajadores> getAllTrabajadores() {
		return trabajadorRe.findAll();
	}

	@Override
	public Trabajadores seleccionarEmailTrabajadores(String correo) {
		return trabajadorRe.findByCorreo(correo);
	}

	@Override
	public Trabajadores seleccionarIdTrabajadores(Integer id) {
		return trabajadorRe.findById(id).orElse(null);
	}

	@Override
	public Trabajadores saveTrabajadores(Trabajadores trabajador) {
		return trabajadorRe.save(trabajador);
	}

	@Override
	public void deleteTrabajadores(Integer id) {
		trabajadorRe.deleteById(id);
	}

}
