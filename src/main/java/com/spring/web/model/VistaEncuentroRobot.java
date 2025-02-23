package com.spring.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "vista_encuentros_robots")
public class VistaEncuentroRobot {

    @Id
    @Column(name = "encuentro_id")
    private Integer encuentroId;

    @Column(name = "fecha_encuentro")
    private String fechaEncuentro;

    @Column(name = "ronda_encuentro")
    private Integer rondaEncuentro;

    @Column(name = "encuentro_finalizado")
    private Boolean encuentroFinalizado;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    @Column(name = "categoria_nombre")
    private String categoriaNombre;
    
    @Column(name = "categoria_formato")
    private String categoriaFormato;

    @Column(name = "es_ganador_1")
    private Integer esGanador1;

    @Column(name = "robot_1_id")
    private Integer robot1Id;

    @Column(name = "robot_1_nombre")
    private String robot1Nombre;

    @Column(name = "es_ganador_2")
    private Integer esGanador2;

    @Column(name = "robot_2_id")
    private Integer robot2Id;

    @Column(name = "robot_2_nombre")
    private String robot2Nombre;

    @Column(name = "resultado")
    private String resultado;
    
    @Column(name = "resultado_robot_1")
    private String resultado_robot_1;
    
    @Column(name = "resultado_robot_2")
    private String resultado_robot_2;
    
    // Getters y Setters
    public Integer getEncuentroId() {
        return encuentroId;
    }

    public void setEncuentroId(Integer encuentroId) {
        this.encuentroId = encuentroId;
    }

    public String getFechaEncuentro() {
        return fechaEncuentro;
    }

    public void setFechaEncuentro(String fechaEncuentro) {
        this.fechaEncuentro = fechaEncuentro;
    }

    public Integer getRondaEncuentro() {
        return rondaEncuentro;
    }

    public void setRondaEncuentro(Integer rondaEncuentro) {
        this.rondaEncuentro = rondaEncuentro;
    }

    public Boolean getEncuentroFinalizado() {
        return encuentroFinalizado;
    }

    public void setEncuentroFinalizado(Boolean encuentroFinalizado) {
        this.encuentroFinalizado = encuentroFinalizado;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Integer getEsGanador1() {
        return esGanador1;
    }

    public void setEsGanador1(Integer esGanador1) {
        this.esGanador1 = esGanador1;
    }

    public Integer getRobot1Id() {
        return robot1Id;
    }

    public void setRobot1Id(Integer robot1Id) {
        this.robot1Id = robot1Id;
    }

    public String getRobot1Nombre() {
        return robot1Nombre;
    }

    public void setRobot1Nombre(String robot1Nombre) {
        this.robot1Nombre = robot1Nombre;
    }

    public Integer getEsGanador2() {
        return esGanador2;
    }

    public void setEsGanador2(Integer esGanador2) {
        this.esGanador2 = esGanador2;
    }

    public Integer getRobot2Id() {
        return robot2Id;
    }

    public void setRobot2Id(Integer robot2Id) {
        this.robot2Id = robot2Id;
    }

    public String getRobot2Nombre() {
        return robot2Nombre;
    }

    public void setRobot2Nombre(String robot2Nombre) {
        this.robot2Nombre = robot2Nombre;
    }

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getResultado_robot_1() {
		return resultado_robot_1;
	}

	public void setResultado_robot_1(String resultado_robot_1) {
		this.resultado_robot_1 = resultado_robot_1;
	}

	public String getResultado_robot_2() {
		return resultado_robot_2;
	}

	public void setResultado_robot_2(String resultado_robot_2) {
		this.resultado_robot_2 = resultado_robot_2;
	}

	public String getCategoriaFormato() {
		return categoriaFormato;
	}

	public void setCategoriaFormato(String categoriaFormato) {
		this.categoriaFormato = categoriaFormato;
	}
	
	
	
    
}
