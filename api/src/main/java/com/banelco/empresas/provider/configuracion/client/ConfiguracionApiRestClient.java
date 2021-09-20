package com.banelco.empresas.provider.configuracion.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientResponse;

import com.banelco.empresas.exception.ConfiguracionException;

@Path("/")
@SuppressWarnings("rawtypes")
public interface ConfiguracionApiRestClient {
	
	@GET
	@Path("services/bank")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ClientResponse getBanco(@QueryParam("code_bcra") String bankCode) throws ConfiguracionException;
	
}
