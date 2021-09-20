package com.banelco.empresas.service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.EmpresaRecargaInfo;
import com.banelco.empresas.util.annotation.NotPrintable;

public interface EmpresaRecargaInfoService {

	@NotPrintable
	EmpresaRecargaInfo obtener(String codigoEmpresa) throws EmpresasApiException;
	
	@NotPrintable
	void evict();
}
