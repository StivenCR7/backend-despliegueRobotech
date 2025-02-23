package com.spring.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name="trabajadores")
public class Trabajadores {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 25)
	private String nombre;
	@Column(length = 25)
	private String apellido;
	
	@NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe contener 9 dígitos")
	@Column(length = 9)
	private String telefono;
	
	@NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido (ejemplo@gmail.com)")
	@Column(unique=true,nullable = false,length = 50)
	
	private String correo;
	
	@NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
        message = "La contraseña debe tener al menos una letra mayúscula, una letra minúscula y un número"
    )
	@Column(length = 225)
	private String contrasena;
	
	//Relacion con roles
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="roles_id")
	private Roles rol;
	
	public Trabajadores() {}

	public Trabajadores(Integer id, String nombre, String apellido,
			@NotBlank(message = "El teléfono es obligatorio") @Pattern(regexp = "\\d{9}", message = "El teléfono debe contener 9 dígitos") String telefono,
			@NotBlank(message = "El correo es obligatorio") @Email(message = "El correo debe tener un formato válido (ejemplo@gmail.com)") String correo,
			@NotBlank(message = "La contraseña es obligatoria") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "La contraseña debe tener al menos una letra mayúscula, una letra minúscula y un número") String contrasena,
			Roles rol) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.correo = correo;
		this.contrasena = contrasena;
		this.rol = rol;
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

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Roles getRol() {
		return rol;
	}

	public void setRol(Roles rol) {
		this.rol = rol;
	}	
}
