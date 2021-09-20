package com.banelco.empresas.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banelco.empresas.model.entity.ImporteDomestico;
import com.banelco.empresas.repository.ImporteDomesticoRepository;
import com.banelco.empresas.service.EmpresaImporteFijoService;

@Service
public class EmpresaImporteFijoServiceImpl implements EmpresaImporteFijoService 
{
	Log logger = LogFactory.getLog(this.getClass());
	
	HashMap<String, ImporteDomestico> importesFijos = null;
	
	@Autowired
	ImporteDomesticoRepository importeDomesticoRepository;
	
	private void obtenerTodas()
	{
		try
		{
			List<ImporteDomestico> importesFijosDB = importeDomesticoRepository.findAll();
			importesFijos = new HashMap<>();
			for (ImporteDomestico importeFijoDB : importesFijosDB)
			{
				importesFijos.put(importeFijoDB.getCodigoEmpresa().trim(), importeFijoDB);
			}
			logger.info("Se obtuvieron " + importesFijos.size() + " registros de la DB ImporteDomestico");
		}
		catch (Exception e)
		{
			logger.error("No se puede obtener listado de ImporteDomestico");
		}
	}
	
	@Override
	public ImporteDomestico obtener(String codigoEmpresa) {
		if(importesFijos == null)
			obtenerTodas();
		
		if(importesFijos == null)
			return null;
		
		return importesFijos.get(codigoEmpresa);
	}

	@Override
	public void evict() 
	{
		this.importesFijos = null;
	}
}
