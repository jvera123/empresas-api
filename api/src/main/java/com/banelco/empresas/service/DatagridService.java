package com.banelco.empresas.service;

import java.util.List;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.rest.response.empresas.EmpresaResponse;
import com.banelco.empresas.util.annotation.NotPrintable;

public interface DatagridService {
	
	@NotPrintable
	public void putList(String key, List<EmpresaResponse> empresas);
	
	@NotPrintable
	public List<EmpresaResponse> getList(String key) throws EmpresasApiException;
	
	public void putCompany(String key, EmpresaResponse empresa);
	
	public EmpresaResponse getCompany(String key) throws EmpresasApiException;
}
