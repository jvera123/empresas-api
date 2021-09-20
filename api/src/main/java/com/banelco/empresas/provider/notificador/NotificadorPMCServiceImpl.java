package com.banelco.empresas.provider.notificador;

import org.jboss.resteasy.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.InstanciaDTO;
import com.banelco.empresas.provider.RestClientFactory;
import com.banelco.empresas.provider.notificador.client.NotificadorMKPClient;
import com.banelco.empresas.provider.notificador.client.NotificadorPMCClient;
import com.banelco.empresas.provider.notificador.response.ErrorResponse;
import com.banelco.empresas.service.NotificadorPMCService;

@Service
@PropertySource("classpath:empresas-api.properties")
public class NotificadorPMCServiceImpl implements NotificadorPMCService {

	@Autowired
	private Environment env;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void notificarInstancia(InstanciaDTO instanciaDTO) throws EmpresasApiException {
		int connectiontimeout = env.getProperty("empresas.notificadorPMC.connectiontimeout", Integer.class);
		int receivetimeout = env.getProperty("empresas.notificadorPMC.receivetimeout", Integer.class);
		try {
			if ("DEMO".equals(instanciaDTO.getFiid())) {
				NotificadorPMCClient client;
				ClientResponse clientResponse;
				client = RestClientFactory.create(instanciaDTO.getPmctasUrl(), connectiontimeout, receivetimeout,
						NotificadorPMCClient.class);
				clientResponse = client.actualizarListas();
				if (clientResponse.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
					ErrorResponse errorResponse = (ErrorResponse) clientResponse.getEntity(ErrorResponse.class);
					throw new EmpresasApiException(errorResponse.getCodigo(), errorResponse.getMensaje());
				}
				client = RestClientFactory.create(instanciaDTO.getMobileUrl(), connectiontimeout, receivetimeout,
						NotificadorPMCClient.class);
				clientResponse = client.actualizarListas();
				if (clientResponse.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
					ErrorResponse errorResponse = (ErrorResponse) clientResponse.getEntity(ErrorResponse.class);
					throw new EmpresasApiException(errorResponse.getCodigo(), errorResponse.getMensaje());
				}
				client = RestClientFactory.create(instanciaDTO.getServicesUrl(), connectiontimeout, receivetimeout,
						NotificadorPMCClient.class);
				clientResponse = client.actualizarListas();
				if (clientResponse.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
					ErrorResponse errorResponse = (ErrorResponse) clientResponse.getEntity(ErrorResponse.class);
					throw new EmpresasApiException(errorResponse.getCodigo(), errorResponse.getMensaje());
				}
			} else if ("TPGO".equals(instanciaDTO.getFiid())) {
				NotificadorMKPClient client;
				client = RestClientFactory.create(instanciaDTO.getMkpUrl(), connectiontimeout, receivetimeout,
						NotificadorMKPClient.class);
				ClientResponse clientResponse = client.actualizarListas();
				if (clientResponse.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
					ErrorResponse errorResponse = (ErrorResponse) clientResponse.getEntity(ErrorResponse.class);
					throw new EmpresasApiException(errorResponse.getCodigo(), errorResponse.getMensaje());
				}
			}
		} finally {
			//RestClientFactory.close();
		}
	}
}
