package com.spring.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Clubes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 25)
	private String nombre;
	@Column(length = 35)
	private String direccion;
	@Column(unique=true,nullable = false,length = 50)
	private String correo;
	@Column(length = 9)
	private String telefono;
	@Column(length = 30)
	private String nombre_representante;
	@Column(length = 255)
	private String contrasena;
	
	//Relacion con estados
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="estado_id")
	private Estados estados;
	
	public Clubes() {}

	public Clubes(Integer id, String nombre, String direccion, String correo, String telefono,
			String nombre_representante, String contrasena, Estados estados) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.correo = correo;
		this.telefono = telefono;
		this.nombre_representante = nombre_representante;
		this.contrasena = contrasena;
		this.estados = estados;
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNombre_representante() {
		return nombre_representante;
	}

	public void setNombre_representante(String nombre_representante) {
		this.nombre_representante = nombre_representante;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Estados getEstados() {
		return estados;
	}

	public void setEstados(Estados estados) {
		this.estados = estados;
	}
	
}
