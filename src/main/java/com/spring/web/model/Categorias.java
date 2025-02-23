package com.spring.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Categorias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(length = 30)
    private String nombre;

    @NotNull
    @Column(length = 25)
    private String formato;

    @NotNull
    @Column(length = 4)
    private Integer cantidad;
    @Column(length = 225)
    private String Banner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "torneos_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Ignorar propiedades innecesarias
    private Torneos torneos;

    
    
    public Categorias() {}

   

    public Categorias(Integer id, @NotNull String nombre, @NotNull String formato, @NotNull Integer cantidad,
			String banner, Torneos torneos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.formato = formato;
		this.cantidad = cantidad;
		Banner = banner;
		this.torneos = torneos;
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Torneos getTorneos() {
        return torneos;
    }

    public void setTorneos(Torneos torneos) {
        this.torneos = torneos;
    }



	public String getBanner() {
		return Banner;
	}



	public void setBanner(String banner) {
		Banner = banner;
	}
    
}
