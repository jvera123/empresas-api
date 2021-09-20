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
@Table(name = "ImporteDomestico")
public class ImporteDomestico {
	@Id
	@Column(name = "codigo_empresa", unique = true, nullable = false)
	private String codigoEmpresa;

	@Column(name = "importe")
	private Double importe;
}
