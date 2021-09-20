package com.banelco.empresas.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Estado")
public class Estado {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "fecha")
	private Date fecha;

	@Column(name = "cant_rubros")
	private int cantRubros = 0;

	@Column(name = "cant_sub_rubros")
	private int cantSubRubros = 0;

	@Column(name = "cant_empresas")
	private int cantEmpresas = 0;

	@Column(name = "cant_empresas_error")
	private int cantEmpresasError = 0;

	@Column(name = "cant_prepagos")
	private int cantPrepagos = 0;

	@Column(name = "cant_prepagos_error")
	private int cantPrepagosError = 0;

	@Column(name = "tiempo")
	private String tiempo;

	@Column(name = "institucion")
	private String institucion;
}
