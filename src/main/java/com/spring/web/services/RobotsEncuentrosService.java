package com.spring.web.services;

import com.spring.web.model.RobotsEncuentros;
import com.spring.web.repository.RobotsEncuentrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RobotsEncuentrosService {


    @Autowired
    private RobotsEncuentrosRepository robotsEncuentrosRepository;

 // Obtener los resultados de los robots en un encuentro
    public List<RobotsEncuentros> obtenerResultadosPorEncuentro(Integer encuentroId) {
        return robotsEncuentrosRepository.findByEncuentroId(encuentroId);
    }


}
