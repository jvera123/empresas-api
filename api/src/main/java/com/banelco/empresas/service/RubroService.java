package com.banelco.empresas.service;

import java.util.List;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Rubro;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.response.empresas.v2.CategoryResponse;
import com.banelco.empresas.rest.response.empresas.v2.CompaniesSubItemResponse;
import com.banelco.empresas.rest.response.empresas.v2.CompanyResponse;
import com.banelco.empresas.util.annotation.NotPrintable;

public interface RubroService {

	public List<Rubro> obtenerRubros(String institucion) throws EmpresasApiException;
	
	@NotPrintable
	public Rubro obtenerRubro(String rubroId,String institucion) throws EmpresasApiException;

	public List<Rubro> obtenerRubrosTransacciones(Request request, String idRefresh) throws EmpresasApiException;

	public void actualizarListaRubros(List<Rubro> rubros) throws EmpresasApiException;
	
	public List<Rubro> obtenerMetaRubros(String idRefresh,Request request);
	
	public List<Rubro> obteerRubrosNoPresentes(String idRefresh,String institucion) throws EmpresasApiException;
	
	public void eliminarRubros(String idRefresh,String institucion) throws EmpresasApiException;

	public List<Rubro> obtenerSubRubros(String institucion) throws EmpresasApiException;

	public void eliminarRubrosSinEmpresas() throws EmpresasApiException;
	
	@NotPrintable
	public Rubro obtenerRubroEnRefresh(String rubroId, String fiid) throws EmpresasApiException;
	
	@NotPrintable
	public void evict();
	
	public void evictCache();
	
	public List<CategoryResponse> findAllByBankId(String bank) throws Exception;

	public List<CompaniesSubItemResponse> findAllCompaniesSubItems(List<CategoryResponse> categories, List<CompanyResponse> companies, String rubroSeleccionado) throws Exception;
}
