package com.banelco.empresas.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.EmpresaRecargaInfo;
import com.banelco.empresas.repository.EmpresaRecargaInfoRepository;
import com.banelco.empresas.service.EmpresaRecargaInfoService;

@Service
public class EmpresaRecargaInfoServiceImpl implements EmpresaRecargaInfoService
{
	Log logger = LogFactory.getLog(this.getClass());
	
	HashMap<String, EmpresaRecargaInfo> infoRecarga = null;
	
	@Autowired
	private EmpresaRecargaInfoRepository empresaRecargaInfoRepository;

	private void obtenerTodas()
	{
		try
		{
			List<EmpresaRecargaInfo> infosRecargaDB = empresaRecargaInfoRepository.findAll();
			infoRecarga = new HashMap<>();
			for (EmpresaRecargaInfo infoRecargaDB : infosRecargaDB)
			{
				infoRecarga.put(infoRecargaDB.getCodigoEmpresa().trim(), infoRecargaDB);
			}
			logger.info("Se obtuvieron " + infoRecarga.size() + " registros de la DB EmpresaRecargaInfo");
		}
		catch (Exception e)
		{
			logger.error("No se puede obtener listado de EmpresaDonacionInfo");
		}
	}
	
	@Override
	public EmpresaRecargaInfo obtener(String codigoEmpresa) throws EmpresasApiException
	{
		if(infoRecarga == null)
			obtenerTodas();
		
		if(infoRecarga == null)
			return null;
		
		return infoRecarga.get(codigoEmpresa);
	}
	
	public void evict()
	{
		this.infoRecarga = null;
	}
}
