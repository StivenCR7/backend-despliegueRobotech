package com.spring.web.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spring.web.model.Noticias;
import com.spring.web.repository.NoticiasRepository;

@Service
public class NoticiaServices {

	 private final NoticiasRepository noticiaRepository;

	    public NoticiaServices(NoticiasRepository noticiaRepository) {
	        this.noticiaRepository = noticiaRepository;
	    }

	    public List<Noticias> listarNoticias() {
	        return noticiaRepository.findAll();
	    }

	    public Optional<Noticias> obtenerNoticiaPorId(Long id) {
	        return noticiaRepository.findById(id);
	    }

	    public Noticias guardarNoticia(Noticias noticia) {
	        return noticiaRepository.save(noticia);
	    }

	    public void eliminarNoticia(Long id) {
	        noticiaRepository.deleteById(id);
	    }
	
}
