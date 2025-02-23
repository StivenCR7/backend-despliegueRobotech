package com.spring.web.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class RobotsEncuentros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private int ganador; // 1: Ganador, 0: Perdedor

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "robot_id", nullable = false)
    private Robots robot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Encuentros encuentro;


    public RobotsEncuentros() {}

    public RobotsEncuentros(int ganador, Robots robot, Encuentros encuentro) {
        this.ganador = ganador;
        this.robot = robot;
        this.encuentro = encuentro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGanador() {
        return ganador;
    }

    public void setGanador(int ganador) {
        this.ganador = ganador;
    }

    public Robots getRobot() {
        return robot;
    }

    public void setRobot(Robots robot) {
        this.robot = robot;
    }

    public Encuentros getEncuentro() {
        return encuentro;
    }

    public void setEncuentro(Encuentros encuentro) {
        this.encuentro = encuentro;
    }
}