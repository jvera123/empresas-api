package com.banelco.empresas.service;

import java.util.List;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.PrepagoDTO;
import com.banelco.empresas.model.entity.Prepago;
import com.banelco.empresas.rest.request.Request;

public interface PrepagoService
{
	public List<Prepago> obtenerPrepagos() throws EmpresasApiException;
	
	public List<Prepago> obtenerPrepagosPorRubro(String idRubro) throws EmpresasApiException;

	public List<PrepagoDTO> obtenerPrepagosTransacciones(Request request) throws EmpresasApiException;

	public void actualizarPrepagos(List<Prepago> empresas) throws EmpresasApiException;
	
	public void elminarPrepagosDeUnRubro(String idRubro) throws EmpresasApiException;
	
	public void elminarPrepagos(String idRefresh) throws EmpresasApiException;

	public void evictCache();
}
