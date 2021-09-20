package com.banelco.empresas.service;

import java.util.List;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.response.transacciones.EmpresaResponseTX;
import com.banelco.empresas.rest.response.transacciones.PrepagoResponseTX;
import com.banelco.empresas.rest.response.transacciones.RubroResponseTX;

public interface TransaccionesAPIService {

	public List<RubroResponseTX> obtenerRubros(Request request) throws EmpresasApiException;

	public List<EmpresaResponseTX> obtenerEmpresas(Request request) throws EmpresasApiException;
	
	public List<PrepagoResponseTX> obtenerPrepagos(Request request) throws EmpresasApiException;
}
