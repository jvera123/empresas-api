package com.banelco.empresas.provider.notificador.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientResponse;

@Path("api/empresasService/")
@SuppressWarnings("rawtypes")
public interface NotificadorPMCClient {
	@GET
	@Path("actualizarListas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ClientResponse actualizarListas();
}
