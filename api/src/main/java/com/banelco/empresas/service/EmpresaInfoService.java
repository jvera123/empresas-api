package com.banelco.empresas.service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.EmpresaInfoDTO;
import com.banelco.empresas.util.annotation.NotPrintable;

public interface EmpresaInfoService
{
	@NotPrintable
	public EmpresaInfoDTO obtenerInfo(String fiid) throws EmpresasApiException;
	@NotPrintable
	public void evict();
}
