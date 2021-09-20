package com.banelco.empresas.service;

import com.banelco.empresas.model.dto.ErrorDTO;

public interface MensajeErrorService {

	ErrorDTO getErrorGenerico();
	
	ErrorDTO getError(String responseCode, String banco, String operacion);
	
	ErrorDTO getError(String responseCode);
	
}
