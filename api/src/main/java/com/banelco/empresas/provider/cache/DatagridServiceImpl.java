package com.banelco.empresas.provider.cache;

import java.util.List;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.provider.RestClientFactory;
import com.banelco.empresas.provider.cache.client.DatagridRestClient;
import com.banelco.empresas.rest.response.empresas.EmpresaResponse;
import com.banelco.empresas.service.DatagridService;
import com.banelco.empresas.util.annotation.NotPrintable;

@Service
@PropertySource("classpath:empresas-api.properties")
public class DatagridServiceImpl implements DatagridService {

	DatagridRestClient client;

	@Autowired
	private Environment env;

	private DatagridRestClient getClient() {
		if (client == null) {
			client = RestClientFactory.create(env.getProperty("datagrid.url"),
					env.getProperty("datagrid.connectiontimeout", Integer.class),
					env.getProperty("datagrid.receivetimeout", Integer.class), DatagridRestClient.class);
		}
		return client;
	}

	@NotPrintable
	public void putList(String key, List<EmpresaResponse> empresas) {
		getClient().putList(key, empresas);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotPrintable
	public List<EmpresaResponse> getList(String key) throws EmpresasApiException {
		ClientResponse response = getClient().getList(key);
		if (ClientResponse.Status.OK.getStatusCode() == response.getStatus()) {
			return (List<EmpresaResponse>) response.getEntity(new GenericType<List<EmpresaResponse>>() {
			});
		}
		throw new EmpresasApiException("Error obteniendo lista de empresas del Datagrid", "DG01");
	}

	@Override
	public void putCompany(String key, EmpresaResponse empresa) {
		getClient().putCompany(key, empresa);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public EmpresaResponse getCompany(String key) throws EmpresasApiException {
		ClientResponse response = getClient().getCompany(key);
		if (ClientResponse.Status.OK.getStatusCode() == response.getStatus()) {
			return (EmpresaResponse) response.getEntity(EmpresaResponse.class);
		} else if (ClientResponse.Status.NOT_FOUND.getStatusCode() == response.getStatus()) {
			throw new EmpresasApiException("Empresa " + key + " no encontrada", "DG02");
		}
		throw new EmpresasApiException("Error obteniendo empresa " + key + " del Datagrid", "DG03");
	}
}
