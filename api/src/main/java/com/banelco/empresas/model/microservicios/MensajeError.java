package com.banelco.empresas.model.microservicios;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MensajeError")
public class MensajeError {

	@EmbeddedId
	private MensajeErrorPrimaryKeyCompuesta id;

	private String codigo;

	private String mensaje;

	private String severidad;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getSeveridad() {
		return severidad;
	}

	public void setSeveridad(String severidad) {
		this.severidad = severidad;
	}

	public MensajeErrorPrimaryKeyCompuesta getId() {
		return id;
	}

	public void setId(MensajeErrorPrimaryKeyCompuesta id) {
		this.id = id;
	}

}
