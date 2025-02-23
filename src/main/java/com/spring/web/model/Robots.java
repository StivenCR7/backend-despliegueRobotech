package com.spring.web.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Robots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 25)
    private String nombre;
    @Column(length = 35)
    private String peso;
    @Column(length = 35)
    private String dimensiones;
    @Column(length = 225)
    private String foto;

    // Relación con Estados
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estados_id")
    private Estados estados;

    // Relación con Competidores
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "competidores_id")
    @JsonManagedReference
    private Competidores competidores;

    // Relación con Categorías
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorias_id")
    @JsonManagedReference
    private Categorias categorias;

    // Relación con RobotsEncuentros
    @OneToMany(mappedBy = "robot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference 
    private List<RobotsEncuentros> robotsEncuentros = new ArrayList<>(); // Inicialización de la lista

    @OneToOne(mappedBy = "robot")
    @JsonBackReference // Evita el bucle con Participaciones
    private Participaciones participaciones;

    
    public Robots() {}

    public Robots(Integer id, String nombre, String peso, String dimensiones, String foto, Estados estados,
                  Competidores competidores, Categorias categorias, List<RobotsEncuentros> robotsEncuentros) {
        this.id = id;
        this.nombre = nombre;
        this.peso = peso;
        this.dimensiones = dimensiones;
        this.foto = foto;
        this.estados = estados;
        this.competidores = competidores;
        this.categorias = categorias;
        this.robotsEncuentros = robotsEncuentros;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }

    public Competidores getCompetidores() {
        return competidores;
    }

    public void setCompetidores(Competidores competidores) {
        this.competidores = competidores;
    }

    public Categorias getCategorias() {
        return categorias;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public List<RobotsEncuentros> getRobotsEncuentros() {
        return robotsEncuentros;
    }

    public void setRobotsEncuentros(List<RobotsEncuentros> robotsEncuentros) {
        this.robotsEncuentros = robotsEncuentros;
    }
    
  
}