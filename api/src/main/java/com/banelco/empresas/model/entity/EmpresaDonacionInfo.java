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
@Table(name = "EmpresaDonacionInfo")
public class EmpresaDonacionInfo {

	@Id
	@Column(name = "codigo_empresa", length = 4)
	private String codigoEmpresa;

	@Column(name = "en_vidriera_donacion")
	private Boolean enVidrieraDonacion;

	@Column(name = "url_vidriera_donacion")
	private String urlVidrieraDonacion;

	@Column(name = "url_imagen_descripcion_vidriera_donacion")
	private String urlImagenDescripcionVidrieraDonacion;

	@Column(name = "orden_vidriera_donacion")
	private Integer ordenVidrieraDonacion;

	@Column(name = "url_pagina_donacion")
	private String urlPaginaDonacion;

	@Column(name = "descripcion_vidriera_donacion", length = 2000)
	private String descripcionVidrieraDonacion;
}
