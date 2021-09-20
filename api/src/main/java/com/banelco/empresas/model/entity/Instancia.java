package com.banelco.empresas.model.entity;

import java.util.Date;

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
@Table(name = "Instancia")
public class Instancia {
	@EmbeddedId
	private InstanciaId instanciaId;

	@Column(name = "fiid", length = 4, nullable = false)
	private String fiid;

	@Column(name = "canal", length = 1, nullable = false)
	private String canal;
	
	@Column(name = "notification_url", nullable = true)
	private String notificationUrl;

	@Column(name = "fecha")
	private Date fecha;

	@Column(name = "resultado")
	private String resultado;

	@Column(name = "tiempo")
	private String tiempo;
}