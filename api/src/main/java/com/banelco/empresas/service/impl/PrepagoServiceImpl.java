package com.banelco.empresas.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.PrepagoDTO;
import com.banelco.empresas.model.entity.Prepago;
import com.banelco.empresas.repository.PrepagoRepository;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.response.transacciones.PrepagoResponseTX;
import com.banelco.empresas.service.PrepagoService;
import com.banelco.empresas.service.TransaccionesAPIService;
import com.banelco.empresas.util.constants.Constantes;
import com.banelco.empresas.util.converters.NombreEmpresaConverter;

@Service
@PropertySource("classpath:empresas-api.properties")
public class PrepagoServiceImpl implements PrepagoService {

	@Autowired
	private PrepagoRepository prepagoRepository;

	@Autowired
	private TransaccionesAPIService clientTransacciones;

	@CacheEvict(value = { "prepagos" }, allEntries = true)
	public void evictCache() {
	}
	
	public List<Prepago> obtenerPrepagos() throws EmpresasApiException {
		List<Prepago> prepagos = prepagoRepository.findAll();
		if (prepagos == null) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_PREPAGOS, Constantes.ERROR_CODIGO_GENERICO);
		}
		return prepagos;
	}

	public List<Prepago> obtenerPrepagosPorRubro(String idRubro) throws EmpresasApiException {
		List<Prepago> prepagos = prepagoRepository.obtenerPrepagosPorRubro(idRubro);
		if (prepagos == null) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_PREPAGOS, Constantes.ERROR_CODIGO_GENERICO);
		}
		return prepagos;
	}

	public List<PrepagoDTO> obtenerPrepagosTransacciones(Request request) throws EmpresasApiException {
		List<PrepagoDTO> prepagos = new ArrayList<PrepagoDTO>();
		List<PrepagoResponseTX> prepagosResponse = clientTransacciones.obtenerPrepagos(request);
		for (PrepagoResponseTX prepagoResponse : prepagosResponse) {
			prepagos.add(toPrepagoDTO(prepagoResponse));
		}
		return prepagos;
	}

	public void actualizarPrepagos(List<Prepago> prepagos) throws EmpresasApiException {
		try {
			prepagoRepository.save(prepagos);
			prepagoRepository.flush();
		} catch (Exception e) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_PERSIST_PREPAGOS, Constantes.ERROR_CODIGO_GENERICO);
		}
	}

	public void elminarPrepagosDeUnRubro(String idRubro) throws EmpresasApiException {
		prepagoRepository.eliminarPrepagosDeUnRubro(idRubro);
	}

	public void elminarPrepagos(String idRefresh) throws EmpresasApiException {
		prepagoRepository.eliminarPrepagos(idRefresh);
	}

	private PrepagoDTO toPrepagoDTO(PrepagoResponseTX response) {
		PrepagoDTO empresa = new PrepagoDTO();
		empresa.setCodigo(response.getId());
		empresa.setDatosTicket(response.getDatosTicket());
		empresa.setDesc(NombreEmpresaConverter.convert(response.getDesc()));
		empresa.setMontos(empresa.toStringMonto(response.getMontos()));
		empresa.setNroLeyenda(response.getNroLeyenda());
		empresa.setOrden(response.getOrden());
		empresa.setRubroId(response.getRubroId());
		return empresa;
	}
}