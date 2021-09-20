package com.banelco.empresas.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banelco.empresas.model.entity.EmpresaConsultaDeudaEnLinea;
import com.banelco.empresas.repository.EmpresaDeudaEnLineaRepository;
import com.banelco.empresas.service.EmpresaDeudaEnLineaService;

@Service
public class EmpresaDeudaEnLineaServiceImpl implements EmpresaDeudaEnLineaService {

	Log logger = LogFactory.getLog(this.getClass());

	Map<String, EmpresaConsultaDeudaEnLinea> infoDeudaEnLineaMap = null;

	@Autowired
	private EmpresaDeudaEnLineaRepository empresaDeudaEnLineaRepository;

	private void obtenerTodas()
	{
		try
		{
			List<EmpresaConsultaDeudaEnLinea> infoDeudaEnLineaList = empresaDeudaEnLineaRepository.findAll();
			infoDeudaEnLineaMap = new HashMap<>();
			for (EmpresaConsultaDeudaEnLinea empresa : infoDeudaEnLineaList)
			{
				infoDeudaEnLineaMap.put(empresa.getCodigoEmpresa().trim(), empresa);
			}
			logger.info("Se obtuvieron " + infoDeudaEnLineaMap.size() + " registros de la DB EmpresaConsultaDeudaEnLinea");
		}
		catch (Exception e)
		{
			logger.error("No se puede obtener listado de EmpresaConsultaDeudaEnLinea");
		}
	}

	@Override
	public EmpresaConsultaDeudaEnLinea obtener(String codigoEmpresa)
	{
		if(infoDeudaEnLineaMap == null)
			obtenerTodas();

		if(infoDeudaEnLineaMap == null)
			return null;

		return infoDeudaEnLineaMap.get(codigoEmpresa);
	}

	public void evict()
	{
		this.infoDeudaEnLineaMap = null;
	}
}