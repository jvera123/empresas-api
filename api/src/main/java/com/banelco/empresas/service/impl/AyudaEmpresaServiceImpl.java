package com.banelco.empresas.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banelco.empresas.model.entity.AyudaEmpresa;
import com.banelco.empresas.repository.AyudaEmpresaRepository;
import com.banelco.empresas.service.AyudaEmpresaService;

@Service
public class AyudaEmpresaServiceImpl implements AyudaEmpresaService
{
	Log logger = LogFactory.getLog(this.getClass());
	
	HashMap<String, String> ayudas = null;
	
	@Autowired
	private AyudaEmpresaRepository ayudaEmpresaRepository;
	
	private void obtenerTodas()
	{
		try
		{
			List<AyudaEmpresa> ayudasDB = ayudaEmpresaRepository.findAll();
			ayudas = new HashMap<>();
			for(AyudaEmpresa ayudaDB : ayudasDB)
			{
				ayudas.put(ayudaDB.getCodigoEmpresa().trim(), ayudaDB.getAyuda());
			}
			logger.info("Se obtuvieron " + ayudas.size() + " registros de la DB Ayudas");
		}
		catch (Exception e)
		{
			logger.error("No se puede obtener listado de AyudaEmpresas");
		}
	}
	
	public String obtener(String codigoEmpresa)
	{
		if(ayudas == null)
			obtenerTodas();
		
		if(ayudas == null)
			return null;
		
		return ayudas.get(codigoEmpresa);
	}

	@Override
	public void guardar(AyudaEmpresa ayudaEmpresa) 
	{
		ayudaEmpresaRepository.save(ayudaEmpresa);
	}
	
	public void evict()
	{
		this.ayudas = null;
	}
}
