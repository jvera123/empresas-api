package com.banelco.empresas.service;

import com.banelco.empresas.model.entity.EmpresaConsultaDeudaEnLinea;
import com.banelco.empresas.util.annotation.NotPrintable;

public interface EmpresaDeudaEnLineaService {

	@NotPrintable
	EmpresaConsultaDeudaEnLinea obtener(String codigoEmpresa);

	@NotPrintable
	void evict();

}