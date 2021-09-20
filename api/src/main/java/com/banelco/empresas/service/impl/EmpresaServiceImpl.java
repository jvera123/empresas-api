package com.banelco.empresas.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.EmpresaDTO;
import com.banelco.empresas.model.entity.Empresa;
import com.banelco.empresas.repository.EmpresaRepository;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.response.transacciones.EmpresaResponseTX;
import com.banelco.empresas.service.EmpresaService;
import com.banelco.empresas.service.TransaccionesAPIService;
import com.banelco.empresas.util.constants.Constantes;
import com.banelco.empresas.util.converters.NombreEmpresaConverter;

@Service
@PropertySource("classpath:empresas-api.properties")
public class EmpresaServiceImpl implements EmpresaService {
	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private TransaccionesAPIService clientTransacciones;

	public List<Empresa> obtenerEmpresas(String institucion) throws EmpresasApiException {
		List<Empresa> empresas = empresaRepository.obtenerEmpresasPorInstitucion(institucion);
		if (empresas == null) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_EMPRESAS, Constantes.ERROR_CODIGO_GENERICO);
		}
		return empresas;
	}

	public List<Empresa> obtenerEmpresasPorRubro(String idRubro, String institucion) throws EmpresasApiException {

		List<Empresa> empresas = empresaRepository
				.findByIdEmbeddedIdEmpresaFiidAndRubroIdEmbeddedIdRubroIdRubroAndRubroIdEmbeddedIdRubroFiid(institucion,
						idRubro, institucion);

		if (empresas == null) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_EMPRESAS, Constantes.ERROR_CODIGO_GENERICO);
		}
		return empresas;
	}

	public List<EmpresaDTO> obtenerEmpresasTransacciones(Request request) throws EmpresasApiException {
		List<EmpresaDTO> empresas = new ArrayList<EmpresaDTO>();
		List<EmpresaResponseTX> empresasResponse = clientTransacciones.obtenerEmpresas(request);
		for (EmpresaResponseTX empresaResponse : empresasResponse) {
			empresas.add(toEmpresaDTO(empresaResponse, request));
		}
		return empresas;
	}

	public void actualizarEmpresas(List<Empresa> empresas) throws EmpresasApiException {
		try {
			empresaRepository.save(empresas);
			empresaRepository.flush();
		} catch (Exception e) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_PERSIST_EMPRESAS, Constantes.ERROR_CODIGO_GENERICO);
		}
	}

	public void eliminarEmpresasDeUnRubro(String idRubro, String institucion) throws EmpresasApiException {
		empresaRepository.eliminarEmpresasDeUnRubro(idRubro, institucion);
	}

	public void eliminarEmpresas(String idRefresh, String institucion) throws EmpresasApiException {
		empresaRepository.eliminarEmpresas(idRefresh, institucion);
	}

	private EmpresaDTO toEmpresaDTO(EmpresaResponseTX empresaResponse, Request request) {
		EmpresaDTO empresa = new EmpresaDTO();
		empresa.setCodigo(empresaResponse.getId());
		empresa.setNombre(NombreEmpresaConverter.convert(empresaResponse.getDesc()));
		empresa.setRubroID(empresaResponse.getRubroId());
		empresa.setDatoAdicional(empresaResponse.getIdentif());
		empresa.setTipoAdhesion(Integer.valueOf(empresaResponse.getTipoAdhe()));
		empresa.setImportePermitido(Integer.valueOf(empresaResponse.getImportePermitido()));
		empresa.setTipoPago(empresaResponse.getTipoPago());
		empresa.setTipoEmpresa(empresaResponse.getTipoEmpresa());
		empresa.setCodigoMoneda(Integer.valueOf(empresaResponse.getMoneda()));
		empresa.setDatoAdicional(empresaResponse.getDatosAdic());
		empresa.setOrden(Integer.valueOf(empresaResponse.getOrden()));
		empresa.setSoloConsultas(empresaResponse.isSoloConsulta());
		empresa.setFiid(empresaResponse.getFiid());
		empresa.setPermitePagosRecurrentes(empresaResponse.isPermitePagosRecurrentes());
		empresa.setPermiteDobleFacturacion(empresaResponse.isPermiteDobleFacturacion());
		empresa.setPermitePagosTCV(empresaResponse.isPermitePagosTCV());
		empresa.setPermitePagosTCM(empresaResponse.isPermitePagosTCM());
		empresa.setPermitePagosTCA(empresaResponse.isPermitePagosTCA());
		empresa.setPermitePagosTDV(empresaResponse.isPermitePagosTDV());
		empresa.setPermitePagosTDM(empresaResponse.isPermitePagosTDM());
		empresa.setMontoMinimoTC(empresaResponse.getMontoMinimoTC());
		empresa.setMontoMaximoTC(empresaResponse.getMontoMaximoTC());
		empresa.setPermiteEBPP(empresaResponse.isPermiteEBPP());
		empresa.setTituloIdentificacion(empresaResponse.getIdentif());
		empresa.setCodigoMoneda(empresaResponse.getMoneda());
		empresa.setPermiteUsd(empresaResponse.isPermiteUsd());
		return empresa;
	}
}