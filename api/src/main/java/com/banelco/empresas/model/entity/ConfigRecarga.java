package com.banelco.empresas.model.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ConfigRecarga")
public class ConfigRecarga {
	@EmbeddedId
	private EmpresaID idEmbeddedIdEmpresa;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "importes")
	private String importes;
}
