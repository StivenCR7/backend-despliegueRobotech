package com.spring.web.repository;

import com.spring.web.model.RobotsEncuentros;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RobotsEncuentrosRepository extends JpaRepository<RobotsEncuentros, Integer> {
	 RobotsEncuentros findByEncuentroIdAndRobotId(Integer encuentroId, Integer robotId); // Para obtener el resultado de un robot en un encuentro
	 List<RobotsEncuentros> findByEncuentroId(Integer encuentroId);

}
