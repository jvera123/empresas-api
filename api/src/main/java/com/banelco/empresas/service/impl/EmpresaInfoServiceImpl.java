package com.banelco.empresas.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.ayudas.EmpresaInfo;
import com.banelco.empresas.model.dto.EmpresaInfoDTO;
import com.banelco.empresas.repository.ayuda.EmpresaInfoRepository;
import com.banelco.empresas.service.EmpresaInfoService;

@Service
@PropertySource("classpath:empresas-api.properties")
public class EmpresaInfoServiceImpl implements EmpresaInfoService
{
	Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private EmpresaInfoRepository empresaInfoRepository;
	
	HashMap<String, EmpresaInfo> empresasInfo = null;
	
	private void obtenerTodas()
	{
		try
		{
			List<EmpresaInfo> empresasInfoDB = empresaInfoRepository.findAll();
			empresasInfo = new HashMap<>();
			for (EmpresaInfo empresaInfo : empresasInfoDB)
			{
				empresasInfo.put(empresaInfo.getCodigo().trim(), empresaInfo);
			}
			logger.info("Se obtuvieron " + empresasInfo.size() + " registros de la DB EmpresaInfo");
		}
		catch (Exception e)
		{
			logger.error("No se puede obtener listado de EmpresaInfo|" + e.getMessage(), e);
		}
	}
	
	public EmpresaInfoDTO obtenerInfo(String fiid) throws EmpresasApiException
	{
		if(empresasInfo == null)
			obtenerTodas();
		
		if(empresasInfo == null)
			return null;
		
		return toAyudaEmpresaDTO(empresasInfo.get(fiid));
	}

	public void evict()
	{
		this.empresasInfo = null;
	}
	
	private EmpresaInfoDTO toAyudaEmpresaDTO(EmpresaInfo ayuda)
	{
		if(ayuda == null)
		{
			return null;
		}
		EmpresaInfoDTO ayudaDTO = new EmpresaInfoDTO();
		ayudaDTO.setCodigo(ayuda.getCodigo());
		ayudaDTO.setCantidadVencimientos(ayuda.getCantidadVencimientos());
		ayudaDTO.setLongitudClaveMaxima(ayuda.getLongitudClaveMaxima());
		ayudaDTO.setLongitudClaveMinima(ayuda.getLongitudClaveMinima());
		ayudaDTO.setCuit(ayuda.getCuit());
		return ayudaDTO;
	}
}
