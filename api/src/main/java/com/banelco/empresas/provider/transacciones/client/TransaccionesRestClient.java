package com.banelco.empresas.provider.transacciones.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.client.ClientResponse;

import com.banelco.empresas.rest.request.Request;

@Path("servicios/")
@SuppressWarnings("rawtypes")
public interface TransaccionesRestClient {

	@POST
	@Path("rubros")
	@Consumes("application/json")
	@Produces({ "application/json" })
	public ClientResponse obtenerRubros(Request request) throws Exception;

	@POST
	@Path("empresas")
	@Consumes("application/json")
	@Produces({ "application/json" })
	public ClientResponse obtenerEmpresas(Request request) throws Exception;

	@POST
	@Path("empresasprepago")
	@Consumes("application/json")
	@Produces({ "application/json" })
	public ClientResponse obtenerPrepagos(Request request) throws Exception;

}
