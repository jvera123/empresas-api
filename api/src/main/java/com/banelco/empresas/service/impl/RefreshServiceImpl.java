package com.banelco.empresas.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.EmpresaDTO;
import com.banelco.empresas.model.dto.EstadoDTO;
import com.banelco.empresas.model.dto.InstanciaDTO;
import com.banelco.empresas.model.dto.PrepagoDTO;
import com.banelco.empresas.model.entity.Empresa;
import com.banelco.empresas.model.entity.EmpresaID;
import com.banelco.empresas.model.entity.Prepago;
import com.banelco.empresas.model.entity.Rubro;
import com.banelco.empresas.model.list.ListasObject;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.service.EmpresaService;
import com.banelco.empresas.service.EstadoService;
import com.banelco.empresas.service.NotificadorPMCService;
import com.banelco.empresas.service.PrepagoService;
import com.banelco.empresas.service.RefreshService;
import com.banelco.empresas.service.RubroService;
import static com.banelco.empresas.util.constants.Constantes.DEMO;

@Service
@PropertySource("classpath:empresas-api.properties")
public class RefreshServiceImpl implements RefreshService
{
	Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private RubroService rubroService;

	@Autowired
	private PrepagoService prepagoService;

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private NotificadorPMCService notificadorService;

	public EstadoDTO refresh(Request request) throws EmpresasApiException
	{
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		String idRefresh = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

		List<Rubro> rubros = null;
		List<Empresa> empresas = null;
		List<EmpresaDTO> empresasDTO = null;
		List<Prepago> prepagos = null;
		List<PrepagoDTO> prepagosDTO = null;
		ListasObject listaObjectAux = null;
		EstadoDTO estadoDTO = new EstadoDTO();

		// Obtiene los Rubros, Empresas Y Prepagos desde BASE24
		logger.info("Obtener Rubros de BASE24 (070000)");
		rubros = rubroService.obtenerRubrosTransacciones(request, idRefresh);

		logger.info("Obtener Empresas de BASE24 (380000)");
		empresasDTO = empresaService.obtenerEmpresasTransacciones(request);

		if (DEMO.equals(request.getFiid()))
		{
			logger.info("Obtener Prepagos de BASE24 (080000)");
			prepagosDTO = prepagoService.obtenerPrepagosTransacciones(request);

			// Agregamos los Meta-Rubros
			logger.info("Agregar Meta-Rubros");
			rubros.addAll(rubroService.obtenerMetaRubros(idRefresh, request));
		}

		// Update de los Rubros a BD
		logger.info("Actualizar Rubros en Base de Datos");
		rubroService.actualizarListaRubros(rubros);

		List<Rubro> rubrosNoPresentes = rubroService.obteerRubrosNoPresentes(idRefresh, request.getFiid());
		for (Rubro rubro : rubrosNoPresentes)
		{
			empresaService.eliminarEmpresasDeUnRubro(rubro.getIdEmbeddedIdRubro().getIdRubro(), request.getFiid());
			prepagoService.elminarPrepagosDeUnRubro(rubro.getIdEmbeddedIdRubro().getIdRubro());
		}

		rubroService.eliminarRubros(idRefresh, request.getFiid());

		listaObjectAux = actualizarRubros(request, empresasDTO, prepagosDTO, idRefresh);
		
		// Update Empresas con Rubros
		logger.info("Actualizar Empresas en Base de Datos");
		empresas = listaObjectAux.getListaCorrecta();
		empresaService.actualizarEmpresas(empresas);
		empresaService.eliminarEmpresas(idRefresh, request.getFiid());

		if (DEMO.equals(request.getFiid()))
		{
			// Update Prepagos con Rubros
			logger.info("Actualizar Prepagos en Base de Datos");
			prepagos = listaObjectAux.getListaCorrectaPrepago();
			prepagoService.actualizarPrepagos(prepagos);
			prepagoService.elminarPrepagos(idRefresh);
		}
		else
		{
			// Eliminar Rubros que no tienen Empresas
			logger.info("Eliminar Rubros son Empresas (TPGO)");
			rubroService.eliminarRubrosSinEmpresas();
			
		}
		
		estadoDTO.setCantSubRubros(rubroService.obtenerSubRubros(request.getFiid()).size());
		estadoDTO.setCantRubros(rubros.size() - estadoDTO.getCantSubRubros());
		estadoDTO.setCantEmpresas(listaObjectAux.getListaCorrecta().size());
		estadoDTO.setCantEmpresasError(listaObjectAux.getListaErronea().size());
		estadoDTO.setCantPrepagos(listaObjectAux.getListaCorrectaPrepago().size());
		estadoDTO.setCantPrepagosError(listaObjectAux.getListaErroneaPrepago().size());
		estadoDTO.setInstitucion(request.getFiid());
		stopWatch.stop();
		estadoDTO.setTiempo(stopWatch.getTotalTimeMillis() + "ms");
		logger.info("Refresh finalizado exitosamente");
		return estadoService.actualizarEstado(estadoDTO);
	}

	public void notificarInstancia(InstanciaDTO instanciaDTO) throws EmpresasApiException
	{
		notificadorService.notificarInstancia(instanciaDTO);
	}

	private ListasObject actualizarRubros(Request request, List<EmpresaDTO> empresasDTO, List<PrepagoDTO> prepagosDTO,
			String idRefresh) throws EmpresasApiException
	{
		List<EmpresaDTO> empresasNoCorrectas = new ArrayList<EmpresaDTO>();
		List<Empresa> empresasCorrectas = new ArrayList<Empresa>();
		List<PrepagoDTO> prepagosNoCorrectas = new ArrayList<PrepagoDTO>();
		List<Prepago> prepagosCorrectas = new ArrayList<Prepago>();

		for (EmpresaDTO empresaDTO : empresasDTO)
		{
			empresaDTO.setRubro(rubroService.obtenerRubroEnRefresh(empresaDTO.getRubroID(), request.getFiid()));
			if (empresaDTO.getRubro() != null)
			{
				empresaDTO.setFiid(request.getFiid());
				empresasCorrectas.add(toEmpresaPrincipal(empresaDTO, idRefresh));
			}
			else
			{
				logger.error("Rubro no encontrado para Empresa: " + empresaDTO.getCodigo() + " - " + empresaDTO.getNombre());
				empresasNoCorrectas.add(empresaDTO);
			}
		}
		
		if (DEMO.equals(request.getFiid()))
		{
			for (PrepagoDTO prepagoDTO : prepagosDTO)
			{
				prepagoDTO.setRubro(rubroService.obtenerRubroEnRefresh(prepagoDTO.getRubroId(), request.getFiid()));
				if (prepagoDTO.getRubro() != null)
				{
					prepagosCorrectas.add(toPrepagoPrincipal(prepagoDTO, idRefresh));
				}
				else
				{
					logger.error("Rubro no encontrado para Prepago: " + prepagoDTO.getCodigo() + " - " + prepagoDTO.getDesc());
					prepagosNoCorrectas.add(prepagoDTO);
				}
			}
		}
		
		rubroService.evict();

		ListasObject listasObjectAux = new ListasObject(empresasCorrectas, empresasNoCorrectas, prepagosCorrectas,
				prepagosNoCorrectas);

		return listasObjectAux;
	}

	private Empresa toEmpresaPrincipal(EmpresaDTO empresaDTO, String idRefresh)
	{
		Empresa empresa = new Empresa();
		EmpresaID empresaID = new EmpresaID();

		empresaID.setCodigo(empresaDTO.getCodigo());
		empresaID.setFiid(empresaDTO.getFiid());
		empresa.setIdEmbeddedIdEmpresa(empresaID);
		empresa.setNombre(empresaDTO.getNombre());
		empresa.setRubro(empresaDTO.getRubro());
		empresa.setDatoAdicional(empresaDTO.getDatoAdicional());
		empresa.setTipoAdhesion(Integer.valueOf(empresaDTO.getTipoAdhesion()));
		empresa.setImportePermitido(Integer.valueOf(empresaDTO.getImportePermitido()));
		empresa.setTipoPago(empresaDTO.getTipoPago());
		empresa.setTipoEmpresa(empresaDTO.getTipoEmpresa());
		empresa.setCodigoMoneda(Integer.valueOf(empresaDTO.getCodigoMoneda()));
		empresa.setDatoAdicional(empresaDTO.getDatoAdicional());
		empresa.setOrden(Integer.valueOf(empresaDTO.getOrden()));
		empresa.setSoloConsultas(empresaDTO.isSoloConsultas());
		empresa.setPermitePagosRecurrentes(empresaDTO.isPermitePagosRecurrentes());
		empresa.setPermiteDobleFacturacion(empresaDTO.isPermiteDobleFacturacion());
		empresa.setPermitePagosTCV(empresaDTO.isPermitePagosTCV());
		empresa.setPermitePagosTCM(empresaDTO.isPermitePagosTCM());
		empresa.setPermitePagosTCA(empresaDTO.isPermitePagosTCA());
		empresa.setPermitePagosTDV(empresaDTO.isPermitePagosTDV());
		empresa.setPermitePagosTDM(empresaDTO.isPermitePagosTDM());
		empresa.setMontoMinimoTC(empresaDTO.getMontoMinimoTC());
		empresa.setMontoMaximoTC(empresaDTO.getMontoMaximoTC());
		empresa.setPermiteEBPP(empresaDTO.isPermiteEBPP());
		empresa.setTituloIdentificacion(empresaDTO.getTituloIdentificacion());
		empresa.setCantidadVencimientos(empresaDTO.getCantidadVencimientos());
		empresa.setLongitudClaveMaxima(empresaDTO.getLongitudClaveMaxima());
		empresa.setLongitudClaveMinima(empresaDTO.getLongitudClaveMinima());
		empresa.setCuit(empresaDTO.getCuit());
		empresa.setPermiteUsd(empresaDTO.isPermiteUsd());
		empresa.setIdRefresh(idRefresh);
		return empresa;
	}

	private Prepago toPrepagoPrincipal(PrepagoDTO prepagoDTO, String idRefresh)
	{
		Prepago prepago = new Prepago();
		prepago.setCodigo(prepagoDTO.getCodigo());
		prepago.setDatosTicket(prepagoDTO.getDatosTicket());
		prepago.setDescripcion(prepagoDTO.getDesc());
		prepago.setMontos(prepagoDTO.getMontos());
		prepago.setNroLeyenda(prepagoDTO.getNroLeyenda());
		prepago.setOrden(prepagoDTO.getOrden());
		prepago.setRubro(prepagoDTO.getRubro());
		prepago.setIdRefresh(idRefresh);
		return prepago;
	}
}
