package com.banelco.empresas.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.EstadoDTO;
import com.banelco.empresas.model.dto.InstanciaDTO;
import com.banelco.empresas.model.entity.Estado;
import com.banelco.empresas.model.entity.Instancia;
import com.banelco.empresas.model.entity.InstanciaId;
import com.banelco.empresas.repository.EstadoRepository;
import com.banelco.empresas.repository.InstanciaRepository;
import com.banelco.empresas.service.EstadoService;
import com.banelco.empresas.util.constants.Constantes;

@Service
@PropertySource("classpath:empresas-api.properties")
public class EstadoServiceImpl implements EstadoService
{
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private InstanciaRepository instanciaRepository;

	public EstadoDTO obtenerEstado() throws EmpresasApiException
	{
		Estado estado = estadoRepository.obtenerEstado();
		return toEstadoDTO(estado);
	}
	
	public List<EstadoDTO> obtenerEstados() throws EmpresasApiException
	{
		List<Estado> estados = estadoRepository.obtenerEstados();
		List<EstadoDTO> estadosDTO = new ArrayList<EstadoDTO>();
		if(estados == null)
		{
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_ESTADOS, Constantes.ERROR_CODIGO_GENERICO);
		}
		for (Estado estado : estados)
		{
			estadosDTO.add(toEstadoDTO(estado));
		}
		return estadosDTO;
	}

	public EstadoDTO actualizarEstado(EstadoDTO estadoDTO) throws EmpresasApiException
	{
		Estado estado = new Estado();
		estado.setFecha(new Date());
		estado.setCantRubros(estadoDTO.getCantRubros());
		estado.setCantSubRubros(estadoDTO.getCantSubRubros());
		estado.setCantEmpresas(estadoDTO.getCantEmpresas());
		estado.setCantEmpresasError(estadoDTO.getCantEmpresasError());
		estado.setCantPrepagos(estadoDTO.getCantPrepagos());
		estado.setCantPrepagosError(estadoDTO.getCantPrepagosError());
		estado.setTiempo(estadoDTO.getTiempo());
		estado.setInstitucion(estadoDTO.getInstitucion());
		estadoRepository.save(estado);
		return obtenerEstado();
	}

	public List<InstanciaDTO> obtenerInstancias() throws EmpresasApiException
	{
		List<Instancia> instancias = instanciaRepository.findAll();
		List<InstanciaDTO> instanciasDTO = new ArrayList<InstanciaDTO>();
		if(instancias == null)
		{
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_INSTANCIAS, Constantes.ERROR_CODIGO_GENERICO);
		}

		HashMap<String, Instancia> map = new HashMap<String, Instancia>();
		Instancia instanciaAux = null;
		String instanciaId = null;
		
		for(Instancia instancia : instancias)
		{
			instanciaId = instancia.getInstanciaId().getIdInstancia();
			instanciaAux = map.get(instanciaId); 
			if(instanciaAux != null)
			{
				if(instancia.getFecha().after(instanciaAux.getFecha()))
				{
					instanciaAux.setFecha(instancia.getFecha());
				}
				instanciaAux.setTiempo(String.valueOf(Long.valueOf(instancia.getTiempo().substring(0, instancia.getTiempo().length() - 2)) + Long.valueOf(instanciaAux.getTiempo().substring(0, instanciaAux.getTiempo().length() - 2)))+"ms");
				instanciaAux.setResultado(instanciaAux.getResultado() + instancia.getInstanciaId().getOperacion() + ": " + instancia.getResultado() + "|");
				map.remove(instanciaId);
				map.put(instanciaId, instanciaAux);
			}
			else 
			{
				instancia.setResultado(instancia.getInstanciaId().getOperacion() + ": " + instancia.getResultado() + "|");
				map.put(instanciaId, instancia);
			}
		}
		
		Estado estado = null;
		InstanciaDTO dto = null;
		
		Iterator<String> keySetIterator = map.keySet().iterator(); 
		while(keySetIterator.hasNext())
		{ 
			instanciaAux = map.get(keySetIterator.next());
			dto = toInstanciaDTO(instanciaAux);
			estado = estadoRepository.obtenerEstadoPorInstitucion(dto.getFiid());
			dto.setActualizar(instanciaAux.getFecha().before(estado.getFecha()));
			instanciasDTO.add(dto);
		}

		Collections.sort(instanciasDTO, new Comparator<InstanciaDTO>()
		{
			public int compare(InstanciaDTO o1, InstanciaDTO o2)
			{
				return o1.getIdInstancia().compareTo(o2.getIdInstancia());
			}
		});
		
		return instanciasDTO;
	}
	
	public InstanciaDTO obtenerInstancia(String idInstancia) throws EmpresasApiException
	{
		Instancia instancia = instanciaRepository.obtenerInstancia(idInstancia);
		if(instancia == null)
		{
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_INSTANCIA, Constantes.ERROR_CODIGO_GENERICO);
		}
		return toInstanciaDTO(instancia);
	}

	public void actualizarInstancia(InstanciaDTO instanciaDTO) throws EmpresasApiException
	{
		Instancia instancia = new Instancia();
		InstanciaId instanciaId = new InstanciaId();
		instanciaId.setIdInstancia(instanciaDTO.getIdInstancia());
		instanciaId.setIp(instanciaDTO.getIp());
		instanciaId.setOperacion(instanciaDTO.getOperacion());
		instancia.setInstanciaId(instanciaId);
		instancia.setFiid(instanciaDTO.getFiid());
		instancia.setCanal(instanciaDTO.getCanal());
		instancia.setNotificationUrl(instanciaDTO.getNotificationUrl());
		instancia.setFecha(new Date());
		instancia.setResultado(instanciaDTO.getResultado());
		instancia.setTiempo(instanciaDTO.getTiempo());
		instanciaRepository.save(instancia);
	}
	
	public void eliminarInstancia(String idInstancia) throws EmpresasApiException
	{
		List<Instancia> instancias = instanciaRepository.obtenerInstanciaAEliminar(idInstancia);
		if(instancias == null)
		{
			throw new EmpresasApiException(Constantes.ERROR_COMANDO_SELECT_INSTANCIA, Constantes.ERROR_CODIGO_GENERICO);
		}
		instanciaRepository.delete(instancias);
	}
	
	private EstadoDTO toEstadoDTO(Estado estado)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		EstadoDTO estadoDTO = new EstadoDTO();
		estadoDTO.setFecha(df.format(estado.getFecha()));
		estadoDTO.setCantRubros(estado.getCantRubros());
		estadoDTO.setCantSubRubros(estado.getCantSubRubros());
		estadoDTO.setCantEmpresas(estado.getCantEmpresas());
		estadoDTO.setCantEmpresasError(estado.getCantEmpresasError());
		estadoDTO.setCantPrepagos(estado.getCantPrepagos());
		estadoDTO.setCantPrepagosError(estado.getCantPrepagosError());
		estadoDTO.setTiempo(estado.getTiempo());
		estadoDTO.setInstitucion(estado.getInstitucion());
		return estadoDTO;
	}
	
	private InstanciaDTO toInstanciaDTO(Instancia instancia)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		InstanciaDTO instanciaDTO = new InstanciaDTO();
		instanciaDTO.setIdInstancia(instancia.getInstanciaId().getIdInstancia());
		instanciaDTO.setIp(instancia.getInstanciaId().getIp());
		instanciaDTO.setOperacion(instancia.getInstanciaId().getOperacion());
		instanciaDTO.setFiid(instancia.getFiid());
		instanciaDTO.setCanal(instancia.getCanal());
		instanciaDTO.setNotificationUrl(instancia.getNotificationUrl());
		instanciaDTO.setFecha(df.format(instancia.getFecha()));
		instanciaDTO.setResultado(instancia.getResultado());
		instanciaDTO.setTiempo(instancia.getTiempo());
		instanciaDTO.setError(instancia.getResultado().contains("ERROR"));
		return instanciaDTO;
	}
}
