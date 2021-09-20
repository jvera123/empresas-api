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
@Table(name = "Rubro")
public class Rubro {
	@EmbeddedId
	private RubroId idEmbeddedIdRubro;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "orden", nullable = true)
	private Integer orden;

	@Column(name = "id_refresh", nullable = false)
	private String idRefresh;

	@Column(name = "habilitado", nullable = false)
	private boolean habilitado = true;
}
