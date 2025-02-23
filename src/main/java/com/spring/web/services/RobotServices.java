package com.spring.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.web.model.Categorias;
import com.spring.web.model.Competidores;
import com.spring.web.model.Estados;
import com.spring.web.model.Robots;
import com.spring.web.repository.CategoriasRepository;
import com.spring.web.repository.CompetidorRepository;
import com.spring.web.repository.EstadosRepository;
import com.spring.web.repository.RobotsRepository;

@Service
public class RobotServices {

	@Autowired
	private RobotsRepository robotRepository;
	@Autowired
	private EstadosRepository estadosRepository;

	@Autowired
	private CategoriasRepository categoriaRepository;

	@Autowired
	private CompetidorRepository competidoRepository;

	public Robots registrarRobotCategoria(Integer categoriaId, Robots robot) {
		// Verificar si la categoría existe
		Categorias categoria = categoriaRepository.findById(categoriaId)
				.orElseThrow(() -> new RuntimeException("La categoría con ID " + categoriaId + " no existe."));

		// Asignar el estado predeterminado
		Estados estadoPorDefecto = estadosRepository.findByNombre("pendiente");
		if (estadoPorDefecto == null) {
			throw new RuntimeException("Estado 'pendiente' no encontrado. Contacte al administrador.");
		}
		robot.setEstados(estadoPorDefecto);

		// Verificar si el competidor existe
		Competidores competidor = competidoRepository.findById(robot.getCompetidores().getId()).orElseThrow(
				() -> new RuntimeException("El competidor con ID " + robot.getCompetidores().getId() + " no existe."));
		robot.setCompetidores(competidor);

		// Asignar categoría al robot
		robot.setCategorias(categoria);

		// Guardar robot
		return robotRepository.save(robot);
	}

	public Robots obtenerRobotID(Integer id) {
		return robotRepository.findById(id).orElseThrow(() -> new RuntimeException("categoria no encontrada"));
	}

	public Robots saveRobot(Robots robot) {
		return robotRepository.save(robot);
	}
	public List<Robots> listarRobotsPorClub(Integer clubId) {
	    return robotRepository.findByCompetidores_Clubes_Id(clubId);
	}

	public List<Robots> listarRobotsPorCompetidor(Integer competidorId ) {
        return robotRepository.findByCompetidores_Id(competidorId);
    }
	public List<Robots> obtenerRobotsPorCategoria(Integer categoriaId) {
	    // Lógica para obtener robots por categoría
	    return robotRepository.findByCategorias_Id(categoriaId);
	}
	public void eliminarRobot(Integer id) {
		robotRepository.deleteById(id);
	}
}