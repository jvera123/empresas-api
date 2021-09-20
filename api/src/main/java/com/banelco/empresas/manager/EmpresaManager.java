package com.banelco.empresas.manager;

import java.util.List;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Empresa;
import com.banelco.empresas.model.entity.Prepago;
import com.banelco.empresas.model.entity.Rubro;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.response.empresas.EmpresaResponse;
import com.banelco.empresas.rest.response.empresas.PrepagoResponse;
import com.banelco.empresas.rest.response.empresas.RubroResponse;
import com.banelco.empresas.rest.response.empresas.v2.CompanyResponse;

public interface EmpresaManager {
	public List<EmpresaResponse> obtenerEmpresas(String institucion) throws EmpresasApiException;

	public List<EmpresaResponse> obtenerEmpresasDeUnRubro(String institucion, String rubro) throws Exception;

	public void establecerEmpresas(Request request) throws EmpresasApiException;

	public List<RubroResponse> convertListToRubroResponse(List<Rubro> rubros);

	public List<EmpresaResponse> convertListToEmpresasResponse(List<Empresa> empresas, List<Empresa> empresasSoloPmc,
			String institucion);

	public List<PrepagoResponse> convertListToPrepagosResponse(List<Prepago> prepagos);

	public RubroResponse obtenerRubroResponse(Rubro rubroPrincipal);

	public EmpresaResponse obtenerEmpresaPorInstitucionYCodigo(String institucion, String codigo) throws Exception;

	public CompanyResponse findCompanyByBankIdAndCompanyId(String institution, String id) throws Exception;

	public List<CompanyResponse> findCompaniesByBankIdAndCategoryId(String institution, String categoryId)
			throws Exception;

	public List<CompanyResponse> findAllCompaniesByBankId(String institution) throws EmpresasApiException;
}
