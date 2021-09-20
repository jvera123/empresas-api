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
@Table(name = "AyudaEmpresa")
public class AyudaEmpresa {
	@Id
	@Column(name = "codigo_empresa")
	private String codigoEmpresa;

	@Column(name = "ayuda")
	private String ayuda;
}
