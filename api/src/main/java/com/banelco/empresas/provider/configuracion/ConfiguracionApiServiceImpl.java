package com.banelco.empresas.provider.configuracion;

import org.jboss.resteasy.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.ConfiguracionException;
import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.Operacion;
import com.banelco.empresas.model.dto.BancoDTO;
import com.banelco.empresas.provider.RestClientFactory;
import com.banelco.empresas.provider.configuracion.client.ConfiguracionApiRestClient;
import com.banelco.empresas.service.ConfiguracionApiService;
import com.banelco.empresas.util.rest.RestUtil;

@Service
@PropertySource("classpath:empresas-api.properties")
public class ConfiguracionApiServiceImpl implements ConfiguracionApiService {

	@Autowired
	private Environment env;

	private ConfiguracionApiRestClient configuracionClient = null;

	public ConfiguracionApiRestClient getClient() {
		if (configuracionClient == null) {
			initClient();
		}
		return configuracionClient;
	}

	private void initClient() {
		configuracionClient = RestClientFactory.create(env.getProperty("configuracion.api.url"),
				env.getProperty("configuracion.api.connectiontimeout", Integer.class),
				env.getProperty("configuracion.api.receivetimeout", Integer.class), ConfiguracionApiRestClient.class);
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	@Cacheable("banks")
	public BancoDTO getBanco(String bankCode) throws ConfiguracionException, EmpresasApiException {
		ClientResponse response = getClient().getBanco(bankCode);
		return RestUtil.obtenerDTO(response, BancoDTO.class, bankCode, Operacion.CONSULTA_BANCO.toString());
	}
	
}
