package com.banelco.empresas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banelco.empresas.model.entity.EmpresaDisplayName;
import com.banelco.empresas.repository.EmpresaDisplayNameRepository;
import com.banelco.empresas.service.EmpresaDisplayNameService;

@Service
public class EmpresaDisplayNameServiceImpl implements EmpresaDisplayNameService{

	@Autowired
	private EmpresaDisplayNameRepository empresaDisplayNameRepository;

	@Override
	public EmpresaDisplayName getEmpresaDisplayName(String codigoEmpresa) {
		return empresaDisplayNameRepository.findOne(codigoEmpresa);
	}
}
