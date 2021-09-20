package com.banelco.empresas.service;

import com.banelco.empresas.model.entity.AyudaEmpresa;
import com.banelco.empresas.util.annotation.NotPrintable;

public interface AyudaEmpresaService
{
	@NotPrintable
	public String obtener(String codigoEmpresa);

	@NotPrintable
	public void guardar(AyudaEmpresa ayudaEmpresa);
	
	@NotPrintable
	public void evict();
}
