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
public class EmpresaID implements Serializable {
	private static final long serialVersionUID = 6292564419585207944L;

	@Column(name = "codigo", nullable = false)
	private String codigo;

	@Column(name = "fiid", length = 4, nullable = false)
	private String fiid;
}
