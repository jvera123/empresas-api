package com.banelco.empresas.service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.InstanciaDTO;

public interface NotificadorPMCService
{
	public void notificarInstancia(InstanciaDTO instanciaDTO) throws EmpresasApiException;
}
