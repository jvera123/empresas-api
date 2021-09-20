package com.banelco.empresas.model.microservicios;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class MensajeErrorPrimaryKeyCompuesta implements Serializable {

	private static final long serialVersionUID = 9164594614957827494L;

	private String rc;

	private String operacion;

	private String banco;

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getRc() {
		return rc;
	}

	public void setRc(String rc) {
		this.rc = rc;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

}
