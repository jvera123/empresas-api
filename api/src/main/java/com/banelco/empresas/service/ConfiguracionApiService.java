package com.banelco.empresas.service;

import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.ConfiguracionException;
import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.BancoDTO;

@Service
public interface ConfiguracionApiService {

	public BancoDTO getBanco(String fiid) throws ConfiguracionException, EmpresasApiException;

}
