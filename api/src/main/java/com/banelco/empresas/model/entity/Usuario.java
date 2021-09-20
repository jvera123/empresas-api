package com.banelco.empresas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Usuario")
public class Usuario {
	@Id
	@Column(name = "nombre_usuario", unique = true, nullable = false)
	private String nombreUsuario;

	@Column(name = "contrasena", nullable = false)
	private String contrasena;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "roles", nullable = false)
	private String roles;
}
