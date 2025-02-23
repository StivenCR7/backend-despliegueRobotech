package com.spring.web.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Encuentros {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	 @Column(nullable = true,length = 30) 
	    private LocalDateTime fecha; // Esta columna será utilizada tanto para la fecha original como la de actualización

	    @NotNull
	    @Column(length = 25)
	    private Integer ronda; // Identifica a qué ronda pertenece el encuentro

	    @NotNull
	    @Column(length = 1)
	    private Boolean esFinalizado;

	    @Column(nullable = true,length = 25)  // Esto es opcional, ya que si lo quieres como un texto para mostrar resultado, puedes usarlo.
	    private String resultado; // Guardará el resultado en formato "1-2" o similar.

	    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "categoria_id", nullable = false)
	    private Categorias categoria;

	    @OneToMany(mappedBy = "encuentro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	    @JsonManagedReference
	    private List<RobotsEncuentros> robotsEncuentros = new ArrayList<>();


    public Encuentros() {}

    public Encuentros(LocalDateTime fecha, Integer ronda, Boolean esFinalizado, Categorias categoria,String resultado) {
        this.fecha = fecha;
        this.ronda = ronda;
        this.esFinalizado = esFinalizado;
        this.categoria = categoria;
        this.resultado = resultado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Integer getRonda() {
        return ronda;
    }

    public void setRonda(Integer ronda) {
        this.ronda = ronda;
    }

    public Boolean getEsFinalizado() {
        return esFinalizado;
    }

    public void setEsFinalizado(Boolean esFinalizado) {
        this.esFinalizado = esFinalizado;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }

    public List<RobotsEncuentros> getRobotsEncuentros() {
        return robotsEncuentros;
    }

    public void setRobotsEncuentros(List<RobotsEncuentros> robotsEncuentros) {
        this.robotsEncuentros = robotsEncuentros;
    }

    public void addRobotEncuentro(RobotsEncuentros robotEncuentro) {
        robotsEncuentros.add(robotEncuentro);
        robotEncuentro.setEncuentro(this);
    }

    public void removeRobotEncuentro(RobotsEncuentros robotEncuentro) {
        robotsEncuentros.remove(robotEncuentro);
        robotEncuentro.setEncuentro(null);
    }

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
    
}
