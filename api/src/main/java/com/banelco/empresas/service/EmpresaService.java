package com.banelco.empresas.service;

import java.util.List;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.EmpresaDTO;
import com.banelco.empresas.model.entity.Empresa;
import com.banelco.empresas.rest.request.Request;

public interface EmpresaService
{
	public List<Empresa> obtenerEmpresas(String institucion) throws EmpresasApiException;
	
	public List<Empresa> obtenerEmpresasPorRubro(String idRubro,String institucion) throws EmpresasApiException;

	public List<EmpresaDTO> obtenerEmpresasTransacciones(Request request) throws EmpresasApiException;

	public void actualizarEmpresas(List<Empresa> empresas) throws EmpresasApiException;
	
	public void eliminarEmpresasDeUnRubro(String idRubro,String institucion) throws EmpresasApiException;
	
	public void eliminarEmpresas(String idRefresh,String institucion) throws EmpresasApiException;
}
