package com.spring.web.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.web.model.Trabajadores;
@Service
public interface TrabajadorServices {

	public List<Trabajadores> getAllTrabajadores();
	
	public Trabajadores seleccionarEmailTrabajadores(String correo);
	
	public Trabajadores seleccionarIdTrabajadores(Integer id);
	
	public Trabajadores saveTrabajadores(Trabajadores trabajador);
	
	void deleteTrabajadores(Integer id);
}
