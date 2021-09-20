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
public class InstanciaId implements Serializable {
	private static final long serialVersionUID = -3580017064494062976L;

	@Column(name = "id_instancia", nullable = false)
	private String idInstancia;

	@Column(name = "ip", nullable = false)
	private String ip;

	@Column(name = "operacion", nullable = false)
	private String operacion;
}
