package com.banelco.empresas.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Institucion;
import com.banelco.empresas.model.entity.Rubro;
import com.banelco.empresas.model.entity.RubroId;
import com.banelco.empresas.model.mapper.CategoryMapper;
import com.banelco.empresas.repository.InstitucionRepository;
import com.banelco.empresas.repository.RubroRepository;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.response.empresas.RubroResponse;
import com.banelco.empresas.rest.response.empresas.v2.CategoryResponse;
import com.banelco.empresas.rest.response.empresas.v2.CompaniesSubItemResponse;
import com.banelco.empresas.rest.response.empresas.v2.CompanyResponse;
import com.banelco.empresas.rest.response.empresas.v2.TipoRubro;
import com.banelco.empresas.rest.response.transacciones.RubroResponseTX;
import com.banelco.empresas.service.RubroService;
import com.banelco.empresas.service.TransaccionesAPIService;
import com.banelco.empresas.util.constants.Constantes;
import com.google.common.primitives.Ints;

import static com.banelco.empresas.util.constants.Constantes.INSTITUTION_NOT_FOUND_ERROR_CODE;
import static com.banelco.empresas.util.constants.Constantes.INSTITUTION_NOT_FOUND_ERROR_MESSAGE;;

@Service
@PropertySource("classpath:empresas-api.properties")
public class RubroServiceImpl implements RubroService {
	Log logger = LogFactory.getLog(this.getClass());

	HashMap<String, Rubro> rubrosEnRefresh = null;

	@Autowired
	private RubroRepository rubroRepository;

	@Autowired
	private TransaccionesAPIService clientTransacciones;
	
	@Autowired
	private InstitucionRepository institucionRepository;		

	@CacheEvict(value = { "rubros" }, allEntries = true)
	public void evictCache() {
	}

	public List<Rubro> obtenerRubros(String institucion) throws EmpresasApiException {
		List<Rubro> rubros = null;

		rubros = rubroRepository.obtenerRubroPorInstitucion(institucion);
		if (rubros == null) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_RUBROS, Constantes.ERROR_CODIGO_GENERICO);
		}
		return rubros;
	}

	public List<Rubro> obtenerSubRubros(String institucion) throws EmpresasApiException {
		List<Rubro> rubros = null;
		rubros = rubroRepository.obtenerSubRubros(institucion);
		if (rubros == null) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_RUBROS, Constantes.ERROR_CODIGO_GENERICO);
		}
		return rubros;
	}

	public Rubro obtenerRubro(String rubroId, String institucion) throws EmpresasApiException {
		try {
			if (Constantes.RUBRO_IEDV.equalsIgnoreCase(rubroId))
				rubroId = Constantes.RUBRO_IEDU;

			List<Rubro> rubros = rubroRepository.obtenerRubro(rubroId, institucion);
			return rubros.get(rubros.size() - 1);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Rubro> obtenerRubrosTransacciones(Request request, String idRefresh) throws EmpresasApiException {
		ArrayList<Rubro> rubros = new ArrayList<Rubro>();
		List<RubroResponseTX> rubrosResponse = clientTransacciones.obtenerRubros(request);
		for (Iterator<RubroResponseTX> iterator = rubrosResponse.iterator(); iterator.hasNext();) {
			RubroResponseTX rubroResponse = (RubroResponseTX) iterator.next();
			Rubro rubro = toRubro(rubroResponse, request.getFiid());
			rubro.setIdRefresh(idRefresh);

			if (!Constantes.RUBRO_IEDV.equalsIgnoreCase(rubro.getIdEmbeddedIdRubro().getIdRubro()))
				rubros.add(rubro);

		}
		return rubros;
	}

	public void actualizarListaRubros(List<Rubro> rubros) throws EmpresasApiException {
		try {
			rubroRepository.save(rubros);
			rubroRepository.flush();
		} catch (Exception e) {
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_PERSIST_RUBROS, Constantes.ERROR_CODIGO_GENERICO);
		}
	}

	public List<Rubro> obtenerMetaRubros(String idRefresh, Request request) {
		List<Rubro> metaRubros = new ArrayList<Rubro>();

		Rubro rubro = null;
		RubroId rubroId = null;

		rubro = new Rubro();
		rubroId = new RubroId();
		rubroId.setIdRubro("1");
		rubroId.setTipo("");
		rubroId.setFiid(request.getFiid());
		rubro.setIdEmbeddedIdRubro(rubroId);
		rubro.setNombre("AFIP");
		rubro.setOrden(0);
		rubro.setIdRefresh(idRefresh);
		metaRubros.add(rubro);

		rubro = new Rubro();
		rubroId = new RubroId();
		rubroId.setIdRubro("2");
		rubroId.setTipo("0");
		rubroId.setFiid(request.getFiid());
		rubro.setIdEmbeddedIdRubro(rubroId);
		rubro.setNombre("Prepagos");
		rubro.setOrden(92);
		rubro.setIdRefresh(idRefresh);
		metaRubros.add(rubro);

		rubro = new Rubro();
		rubroId = new RubroId();
		rubroId.setIdRubro("3");
		rubroId.setTipo("0");
		rubroId.setFiid(request.getFiid());
		rubro.setIdEmbeddedIdRubro(rubroId);
		rubro.setNombre("Tickets");
		rubro.setOrden(93);
		rubro.setIdRefresh(idRefresh);
		metaRubros.add(rubro);

		return metaRubros;
	}

	public List<Rubro> obteerRubrosNoPresentes(String idRefresh, String institucion) throws EmpresasApiException {
		return rubroRepository.obtenerRubrosNoPresentes(idRefresh, institucion);
	}

	public void eliminarRubros(String idRefresh, String institucion) throws EmpresasApiException {
		rubroRepository.eliminarRubros(idRefresh, institucion);
		rubroRepository.flush();
	}

	public void eliminarRubrosSinEmpresas() throws EmpresasApiException {
		List<Rubro> rubros = rubroRepository.obtenerRubrosSinEmpresas();
		rubroRepository.delete(rubros);
		rubroRepository.flush();
	}

	private Rubro toRubro(RubroResponseTX rubroResponse, String fiid) {
		Rubro rubro = new Rubro();
		RubroId rubroID = new RubroId();

		rubroID.setIdRubro(rubroResponse.getId());
		rubroID.setTipo(rubroResponse.getTipo());
		rubroID.setFiid(fiid);

		rubro.setNombre(rubroResponse.getDesc());
		rubro.setOrden(rubroResponse.getOrden());
		rubro.setIdEmbeddedIdRubro(rubroID);
		return rubro;
	}

	private void obtenerTodosRubrosEnrefresh(String fiid) {
		try {
			List<Rubro> rubrosEnRefreshDB = rubroRepository.findByIdEmbeddedIdRubroFiid(fiid);
			rubrosEnRefresh = new HashMap<>();
			for (Rubro rubroEnRefreshDB : rubrosEnRefreshDB) {
				rubrosEnRefresh.put(rubroEnRefreshDB.getIdEmbeddedIdRubro().getIdRubro().trim(), rubroEnRefreshDB);
			}
			logger.info("Se obtuvieron " + rubrosEnRefresh.size() + " registros de la DB Rubro");
		} catch (Exception e) {
			logger.error("No se puede obtener listado de Rubro");
		}
	}

	@Override
	public Rubro obtenerRubroEnRefresh(String rubroId, String fiid) throws EmpresasApiException {
		if (rubrosEnRefresh == null)
			obtenerTodosRubrosEnrefresh(fiid);

		if (rubrosEnRefresh == null)
			return null;

		if (Constantes.RUBRO_IEDV.equalsIgnoreCase(rubroId))
			rubroId = Constantes.RUBRO_IEDU;

		return rubrosEnRefresh.get(rubroId);
	}

	@Override
	public void evict() {
		this.rubrosEnRefresh = null;
	}

	@Override
	public List<CategoryResponse> findAllByBankId(String bank) throws Exception {
		Institucion institucion = institucionRepository.findByConsumerId(bank);
		if (institucion == null) {
			throw new EmpresasApiException(INSTITUTION_NOT_FOUND_ERROR_MESSAGE, INSTITUTION_NOT_FOUND_ERROR_CODE);
		}
		
		List<Rubro> rubros = this.obtenerRubros(institucion.getRefreshFiid());
		return CategoryMapper.map(rubros);
	}
	
	@Override
	public List<CompaniesSubItemResponse> findAllCompaniesSubItems(List<CategoryResponse> categories, 
			List<CompanyResponse> companies, String subItemCheckName) throws Exception {
		
		List<CompaniesSubItemResponse> companiesSubItems = new ArrayList<CompaniesSubItemResponse>();
	
		// tipo de rubro seleccionado
		String typeSubItemCheck = getTypeSubItem(categories, subItemCheckName);
		
		
		for (CategoryResponse rubro : categories) {
			
			CompaniesSubItemResponse companieSubItem = getCompaniesSubItem(rubro, companies);
			if (companieSubItem.getType().equals(typeSubItemCheck)){
				
				companiesSubItems.add(companieSubItem);
			}
		}
		
		
		   
		Collections.sort(companiesSubItems);
		
		
		return filtrarSubRubros(companiesSubItems, typeSubItemCheck);
	}
	
	/**
	 * find id type of "rubros"
	 * @param categories
	 * @param subItemCheckName
	 * @return
	 */
	private String getTypeSubItem(List<CategoryResponse> categories, String subItemCheckName) {
		if (subItemCheckName.isEmpty())
			return null;
		String subItemType = null;
		for (CategoryResponse subItem : categories) {
			if(subItem.getName().equals(subItemCheckName)) {
				subItemType = subItem.getId();
			}
		}
		return subItemType;
	}

	/**
	 * Filtra la lista de subRubros segun el filtro. Si rubroSeleccionado tiene ""
	 * o null retorna toda la lista.
	 * 
	 * @param subRubros
	 *            lista de subrubros
	 * @param rubroSeleccionado
	 *            filtro
	 * @return lista de subrubros filtrados. Si no tiene filtro devuelve todas.
	 */
	private static List<CompaniesSubItemResponse> filtrarSubRubros(List<CompaniesSubItemResponse> subRubros, String rubroSeleccionado) throws Exception
	{
		if (StringUtils.isEmpty(rubroSeleccionado))
		{
			return subRubros;
		}

		List<CompaniesSubItemResponse> result = new ArrayList<CompaniesSubItemResponse>();
		for(CompaniesSubItemResponse rubro:subRubros){
			if (rubro.getType().equals(rubroSeleccionado))
			{
				result.add(rubro);
			}
		}
		Collections.sort(result);
		return result;
	}

	private CompaniesSubItemResponse getCompaniesSubItem(CategoryResponse rubro, List<CompanyResponse> companies) {
		
		CompaniesSubItemResponse companySubItem = new CompaniesSubItemResponse();
		companySubItem.setCode(rubro.getId());
		companySubItem.setType(rubro.getType());
		companySubItem.setOrden(rubro.getOrder());
		companySubItem.setName(rubro.getName());
		
		
	
		// TODO se debe cambiar objeto dummy
		TipoRubro tipoRubro = new TipoRubro();
		tipoRubro.setEmpresaComun(true);
		tipoRubro.setPrepago(true);
		tipoRubro.setConsulta(true);
		companySubItem.setTypeItem(tipoRubro);
		
		
		return companySubItem;
	}


}
