package com.banelco.empresas.service;

import java.util.List;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.EstadoDTO;
import com.banelco.empresas.model.dto.InstanciaDTO;

public interface EstadoService 
{
	public EstadoDTO obtenerEstado() throws EmpresasApiException;
	
	public List<EstadoDTO> obtenerEstados() throws EmpresasApiException;
	
	public EstadoDTO actualizarEstado(EstadoDTO estadoDTO) throws EmpresasApiException;
	
	public List<InstanciaDTO> obtenerInstancias()  throws EmpresasApiException;
	
	public InstanciaDTO obtenerInstancia(String idInstancia)  throws EmpresasApiException;
	
	public void actualizarInstancia(InstanciaDTO instanciaDTO) throws EmpresasApiException;
	
	public void eliminarInstancia(String idInstancia)  throws EmpresasApiException;
}