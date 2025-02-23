package com.spring.web.services;

import java.util.List;

import com.spring.web.model.Estados;

public interface EstadoServices {

	public List<Estados> getAllEstados();
	
	public Estados ObtenerPorIdEstados(Integer id);
	
	public Estados saveEstados(Estados estado);
}
