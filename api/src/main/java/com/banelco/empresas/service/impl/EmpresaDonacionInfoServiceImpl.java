package com.banelco.empresas.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banelco.empresas.model.entity.EmpresaDonacionInfo;
import com.banelco.empresas.repository.EmpresaDonacionInfoRepository;
import com.banelco.empresas.service.EmpresaDonacionInfoService;

@Service
public class EmpresaDonacionInfoServiceImpl implements EmpresaDonacionInfoService
{
	Log logger = LogFactory.getLog(this.getClass());
	
	HashMap<String, EmpresaDonacionInfo> infoDonaciones = null;

	@Autowired
	private EmpresaDonacionInfoRepository empresaDonacionInfoRepository;

	private void obtenerTodas()
	{
		try
		{
			List<EmpresaDonacionInfo> infoDonacionesDB = empresaDonacionInfoRepository.findAll();
			infoDonaciones = new HashMap<>();
			for (EmpresaDonacionInfo infoDonacionDB : infoDonacionesDB)
			{
				infoDonaciones.put(infoDonacionDB.getCodigoEmpresa().trim(), infoDonacionDB);
			}
			logger.info("Se obtuvieron " + infoDonaciones.size() + " registros de la DB EmpresaDonacionInfo");
		}
		catch (Exception e)
		{
			logger.error("No se puede obtener listado de EmpresaDonacionInfo");
		}
	}

	@Override
	public EmpresaDonacionInfo obtener(String codigoEmpresa)
	{
		if(infoDonaciones == null)
			obtenerTodas();
		
		if(infoDonaciones == null)
			return null;
		
		return infoDonaciones.get(codigoEmpresa);
	}
	
	public void evict()
	{
		this.infoDonaciones = null;
	}
}
