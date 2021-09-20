package com.banelco.empresas.provider.cache.client;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientResponse;

import com.banelco.empresas.rest.response.empresas.EmpresaResponse;

@Path("/")
@SuppressWarnings("rawtypes")
public interface DatagridRestClient {
	@PUT
	@Path("{key}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ClientResponse putList(@PathParam("key") String key, List<EmpresaResponse> empresas);
	
	@GET
	@Path("{key}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ClientResponse getList(@PathParam("key") String key);
	
	@PUT
	@Path("{key}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ClientResponse putCompany(@PathParam("key") String key, EmpresaResponse empresa);
	
	@GET
	@Path("{key}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ClientResponse getCompany(@PathParam("key") String key);
}
