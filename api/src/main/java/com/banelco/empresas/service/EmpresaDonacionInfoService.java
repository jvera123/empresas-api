package com.banelco.empresas.service;

import com.banelco.empresas.model.entity.EmpresaDonacionInfo;
import com.banelco.empresas.util.annotation.NotPrintable;

public interface EmpresaDonacionInfoService
{
	@NotPrintable
	EmpresaDonacionInfo obtener(String codigoEmpresa);
	
	@NotPrintable
	void evict();
}
