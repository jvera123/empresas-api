package com.banelco.empresas.service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.EstadoDTO;
import com.banelco.empresas.model.dto.InstanciaDTO;
import com.banelco.empresas.rest.request.Request;

public interface RefreshService
{
	public EstadoDTO refresh(Request request) throws EmpresasApiException;
	
	public void notificarInstancia(InstanciaDTO instanciaDTO) throws EmpresasApiException;
}
