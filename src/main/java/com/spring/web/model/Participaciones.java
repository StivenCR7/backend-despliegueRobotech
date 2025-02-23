package com.spring.web.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Participaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación 1 a 1 con Robot
    @OneToOne
    @JoinColumn(name = "robot_id", referencedColumnName = "id")
    @JsonManagedReference // Se serializa correctamente sin generar bucles
    private Robots robot;


    // Relación 1 a N con Categoria
    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    private Categorias categoria;

    // Campos adicionales
    @Column(length = 4)
    private int victorias;
    @Column(length = 4)
    private int derrotas;
    @Column(length = 25)
    private int posicion_final;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Robots getRobot() {
        return robot;
    }

    public void setRobot(Robots robot) {
        this.robot = robot;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getPosicionFinal() {
        return posicion_final;
    }

    public void setPosicionFinal(int posicionFinal) {
        this.posicion_final = posicionFinal;
    }
}
