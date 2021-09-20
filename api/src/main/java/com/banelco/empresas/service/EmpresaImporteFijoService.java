package com.banelco.empresas.service;

import com.banelco.empresas.model.entity.ImporteDomestico;
import com.banelco.empresas.util.annotation.NotPrintable;

public interface EmpresaImporteFijoService 
{
	@NotPrintable
	ImporteDomestico obtener(String codigoEmpresa);
	
	@NotPrintable
	void evict();
}
