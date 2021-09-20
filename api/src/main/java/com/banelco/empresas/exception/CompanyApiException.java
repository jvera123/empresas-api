package com.banelco.empresas.exception;

import com.banelco.empresas.model.dto.ErrorCompanyDTO;

public class CompanyApiException extends Exception{
	private static final long serialVersionUID = 1L
			;
	private final EmpresasApiException  apiException;
	
	public CompanyApiException(EmpresasApiException apiException) {
		this.apiException = apiException;
	}
	
	public ErrorCompanyDTO getError() {
		String codigoError = "";
		String message=apiException.getMessage();
		
		if ("DG02".equals(apiException.getCodigoError())){
			codigoError="EA38";
			message = "Company id is not valid";
		}
		
		ErrorCompanyDTO errorCompanyDTO = ErrorCompanyDTO.builder()
				.code(codigoError)
				.message(message).build();
		
		return errorCompanyDTO;
	}
}
