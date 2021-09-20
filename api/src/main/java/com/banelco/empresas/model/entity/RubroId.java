package com.banelco.empresas.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RubroId implements Serializable {
	private static final long serialVersionUID = 8271144867768840428L;

	@Column(name = "id_rubro", length = 4, nullable = false)
	private String idRubro;

	@Column(name = "tipo", nullable = false)
	private String tipo;

	@Column(name = "fiid", nullable = false)
	private String fiid;
}