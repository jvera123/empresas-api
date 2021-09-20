package com.banelco.empresas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EmpresaConsultaDeudaEnLinea")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmpresaConsultaDeudaEnLinea {

	@Id
	@Column(name = "codigo_empresa")
	private String codigoEmpresa;

	@Column(name = "descripcion_adicional")
	private String descripcionAdicional;
}