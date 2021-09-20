package com.banelco.empresas.exception;

public class EmpresasApiException extends Exception {
	private static final long serialVersionUID = 6424165829322305303L;

	private String codigoError;

	public EmpresasApiException(String mensaje, String codigoError) {
		super(mensaje);
		this.codigoError = codigoError;
	}

	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

}
