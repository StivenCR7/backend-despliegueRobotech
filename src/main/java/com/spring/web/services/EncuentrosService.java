package com.spring.web.services;


import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.web.model.Categorias;
import com.spring.web.model.Encuentros;
import com.spring.web.model.Estados;
import com.spring.web.model.Robots;
import com.spring.web.model.RobotsEncuentros;
import com.spring.web.repository.EncuentrosRepository;
import com.spring.web.repository.EstadosRepository;
import com.spring.web.repository.RobotsEncuentrosRepository;
import com.spring.web.repository.RobotsRepository;
import com.spring.web.repository.CategoriasRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class EncuentrosService {

    @Autowired
    private EncuentrosRepository encuentrosRepository;

    @Autowired
    private CategoriasRepository categoriasRepository;

    @Autowired
    private EstadosRepository estadorepository;
    
    @Autowired
    private RobotsEncuentrosRepository robotsEncuentrosRepository;
    
    @Autowired
    private RobotsRepository RobotsRepository;
    
    public List<Encuentros> obtenerEncuentrosPorCategoria(Integer categoriaId) {
        return encuentrosRepository.findByCategoriaId(categoriaId);
    }

    public Optional<Encuentros> obtenerEncuentroPorId(Integer id) {
        return encuentrosRepository.findById(id);
    }

    
    /**
     * Genera los encuentros necesarios para una categoría con formato de eliminación directa.
     * 
     * @param categoriaId ID de la categoría.
     */
    public void generarEncuentrosEliminacionDirecta(Integer categoriaId) {
        // Obtener la categoría
        Categorias categoria = categoriasRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Verificar que el formato sea eliminación directa
        if (!categoria.getFormato().equalsIgnoreCase("Eliminación directa")) {
            throw new RuntimeException("El formato de la categoría no es eliminación directa");
        }

        // Obtener la cantidad de robots
        int cantidadRobots = categoria.getCantidad();

        // Calcular el número de rondas necesarias (log2(cantidadRobots))
        int rondas = (int) Math.ceil(Math.log(cantidadRobots) / Math.log(2));

        // Total de encuentros necesarios (cantidadRobots - 1)
        int totalEncuentros = cantidadRobots - 1;

        // Crear los encuentros vacíos
        @SuppressWarnings("unused")
		int encuentroId = 1; // Solo para controlar IDs únicos
        for (int ronda = 1; ronda <= rondas; ronda++) {
            // Cantidad de encuentros en la ronda actual
            int cantidadEncuentrosEnRonda = (int) Math.pow(2, rondas - ronda);

            for (int i = 0; i < cantidadEncuentrosEnRonda; i++) {
                Encuentros encuentro = new Encuentros();
                encuentro.setFecha(LocalDateTime.now()); // Puedes ajustar la fecha si es necesario
                encuentro.setRonda(ronda);
                encuentro.setEsFinalizado(false);
                encuentro.setCategoria(categoria);
encuentro.setResultado(null);
                // Guardar el encuentro
                encuentrosRepository.save(encuentro);
                encuentroId++;
            }
        }

        System.out.println("Encuentros generados: " + totalEncuentros);
    }

    public void asignarRobotsAEncuentrosPrimeraRonda(Integer categoriaId) {
        // Obtener la categoría
        @SuppressWarnings("unused")
		Categorias categoria = categoriasRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Obtener el estado "Aprobado" por su nombre
        Estados estadoAprobado = estadorepository.findByNombre("aprobado");
        if (estadoAprobado == null) {
            throw new RuntimeException("Estado 'Aprobado' no encontrado");
        }

        // Obtener los robots aprobados para la categoría usando el ID del estado "Aprobado"
        List<Robots> robotsAprobados = RobotsRepository.findByCategoriasIdAndEstadosId(categoriaId, estadoAprobado.getId());

        if (robotsAprobados.size() < 2) {
            throw new RuntimeException("No hay suficientes robots aprobados para asignar encuentros");
        }

        // Aleatorizar la lista de robots aprobados
        Collections.shuffle(robotsAprobados);

        // Obtener los encuentros de la primera ronda que ya han sido creados
        List<Encuentros> encuentrosRonda1 = encuentrosRepository.findByCategoriaIdAndRonda(categoriaId, 1);

        if (encuentrosRonda1.size() < robotsAprobados.size() / 2) {
            throw new RuntimeException("No hay suficientes encuentros para la primera ronda");
        }

        // Asignar robots a los encuentros de la primera ronda
        int encuentroIndex = 0;
        for (int i = 0; i < robotsAprobados.size(); i += 2) {
            if (i + 1 < robotsAprobados.size()) {
                // Obtener el encuentro de la primera ronda
                Encuentros encuentro = encuentrosRonda1.get(encuentroIndex);

                // Crear los registros en RobotsEncuentros
                RobotsEncuentros robotEncuentro1 = new RobotsEncuentros(0, robotsAprobados.get(i), encuentro);
                RobotsEncuentros robotEncuentro2 = new RobotsEncuentros(0, robotsAprobados.get(i + 1), encuentro);

                // Guardar los registros en la base de datos
                robotsEncuentrosRepository.save(robotEncuentro1);
                robotsEncuentrosRepository.save(robotEncuentro2);

                // Añadir los robots al encuentro para mantener la relación bidireccional
                encuentro.addRobotEncuentro(robotEncuentro1);
                encuentro.addRobotEncuentro(robotEncuentro2);

                // Guardar el encuentro nuevamente después de agregar los robots
                encuentrosRepository.save(encuentro);

                encuentroIndex++;
            }
        }

        System.out.println("Robots asignados a encuentros de la primera ronda.");
    }

    @Transactional
    public void actualizarGanador(Integer encuentroId, Integer robotGanadorId) {
        // Verificar si el encuentro existe
        Encuentros encuentro = encuentrosRepository.findById(encuentroId)
                .orElseThrow(() -> new IllegalArgumentException("El encuentro con ID " + encuentroId + " no existe."));

        // Verificar si el robot existe
        @SuppressWarnings("unused")
		Robots robotGanador = RobotsRepository.findById(robotGanadorId)
                .orElseThrow(() -> new IllegalArgumentException("El robot con ID " + robotGanadorId + " no existe."));

        // Verificar si el robot pertenece al encuentro
        Optional<RobotsEncuentros> relacionRobotEncuentro = encuentro.getRobotsEncuentros().stream()
                .filter(re -> re.getRobot().getId().equals(robotGanadorId))
                .findFirst();

        if (relacionRobotEncuentro.isEmpty()) {
            throw new IllegalArgumentException("El robot con ID " + robotGanadorId + " no participa en el encuentro con ID " + encuentroId + ".");
        }

        // Actualizar la relación RobotsEncuentros para indicar el ganador
        for (RobotsEncuentros re : encuentro.getRobotsEncuentros()) {
            re.setGanador(re.getRobot().getId().equals(robotGanadorId) ? 1 : 0);
            robotsEncuentrosRepository.save(re);
        }

        // Marcar el encuentro como finalizado
        encuentro.setEsFinalizado(true);
        encuentrosRepository.save(encuentro);

        // Verificar si todos los encuentros de la ronda han terminado
        if (todosEncuentrosFinalizados(encuentro.getCategoria().getId(), encuentro.getRonda())) {
            // Avanzar a la siguiente ronda
            asignarGanadoresARondaSiguiente(encuentro.getCategoria().getId(), encuentro.getRonda());
        }
    }

    public boolean todosEncuentrosFinalizados(Integer categoriaId, Integer ronda) {
        // Obtener todos los encuentros de la categoría y ronda especificada
        List<Encuentros> encuentrosRonda = encuentrosRepository.findByCategoriaIdAndRonda(categoriaId, ronda);
        
        // Verificar si todos los encuentros están finalizados
        for (Encuentros encuentro : encuentrosRonda) {
            if (!encuentro.getEsFinalizado()) {
                return false;  // Si al menos uno no está finalizado, devolvemos false
            }
        }
        return true;  // Todos los encuentros están finalizados
    }



    private void asignarGanadoresARondaSiguiente(Integer categoriaId, Integer ronda) {
        // Obtener los encuentros finalizados de la ronda actual
        List<Encuentros> encuentrosDeLaRonda = encuentrosRepository.findByCategoriaIdAndRonda(categoriaId, ronda);

        // Obtener los robots ganadores junto con sus IDs de encuentro
        List<RobotsEncuentros> robotsGanadores = new ArrayList<>();
        for (Encuentros encuentro : encuentrosDeLaRonda) {
            for (RobotsEncuentros re : encuentro.getRobotsEncuentros()) {
                if (re.getGanador() == 1) {
                    robotsGanadores.add(re); // Agregar el objeto RobotsEncuentros
                }
            }
        }

        // Si hay robots ganadores, ordenarlos por el ID del encuentro
        if (!robotsGanadores.isEmpty()) {
            robotsGanadores.sort(Comparator.comparingInt(re -> re.getEncuentro().getId()));

            int siguienteRonda = ronda + 1;
            List<Encuentros> nuevosEncuentros = encuentrosRepository.findByCategoriaIdAndRonda(categoriaId, siguienteRonda);

            // Asignar los robots ganadores a los encuentros de la siguiente ronda
            int encuentroIndex = 0;
            for (int i = 0; i < robotsGanadores.size(); i += 2) {
                if (i + 1 < robotsGanadores.size()) {
                    Encuentros siguienteEncuentro = nuevosEncuentros.get(encuentroIndex);
                    RobotsEncuentros re1 = new RobotsEncuentros(0, robotsGanadores.get(i).getRobot(), siguienteEncuentro);
                    RobotsEncuentros re2 = new RobotsEncuentros(0, robotsGanadores.get(i + 1).getRobot(), siguienteEncuentro);

                    robotsEncuentrosRepository.save(re1);
                    robotsEncuentrosRepository.save(re2);

                    siguienteEncuentro.addRobotEncuentro(re1);
                    siguienteEncuentro.addRobotEncuentro(re2);

                    encuentrosRepository.save(siguienteEncuentro);

                    encuentroIndex++;
                }
            }
        }
    }

 // Método para actualizar el resultado y la fecha con hora
    public Encuentros actualizarResultadoYFecha(Integer encuentroId, String resultado, LocalDateTime fecha) {
        Encuentros encuentro = encuentrosRepository.findById(encuentroId)
                .orElseThrow(() -> new RuntimeException("Encuentro no encontrado"));
        encuentro.setResultado(resultado);
        encuentro.setFecha(fecha); // Actualiza la fecha con la nueva fecha y hora proporcionada
        return encuentrosRepository.save(encuentro);
    }
    
}
