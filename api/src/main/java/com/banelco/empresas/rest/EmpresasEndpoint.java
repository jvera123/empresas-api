package com.banelco.empresas.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.manager.EmpresaManager;
import com.banelco.empresas.model.dto.ErrorDTO;
import com.banelco.empresas.model.dto.EstadoDTO;
import com.banelco.empresas.model.dto.InstanciaDTO;
import com.banelco.empresas.model.entity.AyudaEmpresa;
import com.banelco.empresas.model.entity.Prepago;
import com.banelco.empresas.model.entity.Rubro;
import com.banelco.empresas.model.entity.Usuario;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.request.UsuarioRequest;
import com.banelco.empresas.rest.response.empresas.EmpresaResponse;
import com.banelco.empresas.rest.response.empresas.PrepagoResponse;
import com.banelco.empresas.rest.response.empresas.RubroResponse;
import com.banelco.empresas.rest.response.usuario.UsuarioResponse;
import com.banelco.empresas.service.AyudaEmpresaService;
import com.banelco.empresas.service.EstadoService;
import com.banelco.empresas.service.PrepagoService;
import com.banelco.empresas.service.RefreshService;
import com.banelco.empresas.service.RubroService;
import com.banelco.empresas.service.UsuarioService;
import com.banelco.empresas.util.constants.Constantes;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Path("/")
@Api(value = "empresas-api")
public class EmpresasEndpoint {
	@Autowired
	private EmpresaManager empresaManager;

	@Autowired
	private RubroService rubroService;

	@Autowired
	private PrepagoService prepagoService;

	@Autowired
	private RefreshService refreshService;

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AyudaEmpresaService ayudaEmpresaService;

	@POST
	@Path("/echo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna el mensaje del usuario", notes = "Retorna el mensaje enviado por el usuario.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Retorno exitoso del mensaje enviado por el usuario.") })
	public Response echo(@Context HttpServletRequest requestContext,
			@ApiParam(name = "mensaje", value = "Mensaje a retornar por el servicio.", required = true) String mensaje) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode respuestaJson = objectMapper.createObjectNode();
		respuestaJson.put("Mensaje Recibido", mensaje);
		respuestaJson.put("IP Origen", obtenerIPAddress(requestContext));
		return Response.status(Response.Status.OK).entity(respuestaJson.toString()).build();
	}

	@POST
	@Path("/autenticar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Autentica un usuario", notes = "Autentica un usuario a partir de un nombre de usuario y contraseña")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = UsuarioResponse.class),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response autenticar(@ApiParam(value = "UsuarioRequest", required = true) UsuarioRequest usuarioRequest)
			throws Exception {
		Usuario usuario = usuarioService.autenticar(usuarioRequest.getNombreUsuario(), usuarioRequest.getContrasena());
		UsuarioResponse usuarioResponse = convertToUsuarioResponse(usuario);
		return Response.status(Response.Status.OK.getStatusCode()).entity(usuarioResponse).type("application/json")
				.build();
	}

	@POST
	@Path("/rubros")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna una lista de Rubros", notes = "Retorna una lista de Rubros.", response = RubroResponse.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = RubroResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerRubros(@Context HttpServletRequest requestContext,
			@ApiParam(value = "La consulta se realiza enviando el FIID, Canal e Instancia", required = true) Request request)
			throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<Rubro> rubros;
		List<RubroResponse> rubrosResponse;
		InstanciaDTO instanciaDTO = new InstanciaDTO(request.getInstancia(), request.getIp(), Constantes.OPERACION_ALL,
				request.getFiid(), request.getCanal(), request.getNotificationUrl());
		try {
			rubros = (List<Rubro>) rubroService.obtenerRubros(request.getFiid());
			rubrosResponse = empresaManager.convertListToRubroResponse(rubros);
			registrarInstancia(instanciaDTO, Constantes.SUCCESS_OPERACION, stopWatch);
			return Response.status(Response.Status.OK.getStatusCode()).entity(rubrosResponse).type("application/json")
					.build();
		} catch (EmpresasApiException e) {
			registrarInstancia(instanciaDTO,
					Constantes.ERROR_OPERACION + "|" + e.getCodigoError() + "|" + e.getMessage(), stopWatch);
			throw e;
		}
	}

	@POST
	@Path("/empresas")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna una lista de Empresas", notes = "Retorna una lista	de Empresas.", response = EmpresaResponse.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = EmpresaResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerEmpresas(@Context HttpServletRequest requestContext,
			@ApiParam(value = "La consulta se realiza enviando el FIID, Canal e Instancia", required = true) Request request)
			throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<EmpresaResponse> empresasResponse;
		InstanciaDTO instanciaDTO = new InstanciaDTO(request.getInstancia(), request.getIp(), Constantes.OPERACION_ALL,
				request.getFiid(), request.getCanal(), request.getNotificationUrl());
		try {
			empresasResponse = empresaManager.obtenerEmpresas(request.getFiid());
			registrarInstancia(instanciaDTO, Constantes.SUCCESS_OPERACION, stopWatch);
			return Response.status(Response.Status.OK.getStatusCode()).entity(empresasResponse).type("application/json")
					.build();
		} catch (EmpresasApiException e) {
			registrarInstancia(instanciaDTO,
					Constantes.ERROR_OPERACION + "|" + e.getCodigoError() + "|" + e.getMessage(), stopWatch);
			throw e;
		}
	}

	@POST
	@Path("/empresas/{idRubro}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna una lista de Empresas de un Rubro", notes = "Retorna una lista de Empresas del Rubro indicado.", response = EmpresaResponse.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = EmpresaResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerEmpresasPorRubro(@Context HttpServletRequest requestContext,
			@ApiParam(value = "Rubro", required = true) @PathParam(value = "idRubro") String idRubro,
			@ApiParam(value = "La consulta se realiza enviando el FIID, Canal e Instancia", required = true) Request request)
			throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<EmpresaResponse> empresasResponse;
		InstanciaDTO instanciaDTO = new InstanciaDTO(request.getInstancia(), request.getIp(), Constantes.OPERACION_ALL,
				request.getFiid(), request.getCanal(), request.getNotificationUrl());
		try {
			empresasResponse = empresaManager.obtenerEmpresasDeUnRubro(request.getFiid(), idRubro);
			registrarInstancia(instanciaDTO, Constantes.SUCCESS_OPERACION, stopWatch);
			return Response.status(Response.Status.OK.getStatusCode()).entity(empresasResponse).type("application/json")
					.build();
		} catch (EmpresasApiException e) {
			registrarInstancia(instanciaDTO,
					Constantes.ERROR_OPERACION + "|" + e.getCodigoError() + "|" + e.getMessage(), stopWatch);
			throw e;
		}
	}

	@POST
	@Path("/empresasPorNombre/{nombre}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna una lista de Empresas cuyo nombre o fiid coincide con la búsqueda", notes = "Retorna una lista de Empresas cuyo nombre o fiid coincide con la búsqueda.", response = EmpresaResponse.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = EmpresaResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerEmpresasPorNombre(@Context HttpServletRequest requestContext,
			@ApiParam(value = "Nombre", required = true) @PathParam(value = "nombre") String nombre,
			@ApiParam(value = "La consulta se realiza enviando el FIID, Canal e Instancia", required = true) Request request)
			throws Exception {
		List<EmpresaResponse> empresasResponse = new ArrayList<EmpresaResponse>();
		List<EmpresaResponse> empresas = empresaManager.obtenerEmpresas(request.getFiid());
		for (EmpresaResponse empresa : empresas) {
			if (StringUtils.containsIgnoreCase(empresa.getNombre(), nombre)
					|| StringUtils.containsIgnoreCase(empresa.getCodigo(), nombre))
				empresasResponse.add(empresa);
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(empresasResponse).type("application/json")
				.build();

	}

	@POST
	@Path("/prepagos")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna una lista de Prepagos", notes = "Retorna una lista	de Prepagos.", response = PrepagoResponse.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = PrepagoResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerPrepagos(@Context HttpServletRequest requestContext,
			@ApiParam(value = "La consulta se realiza enviando el FIID, Canal e Instancia", required = true) Request request)
			throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<Prepago> prepagos;
		List<PrepagoResponse> prepagosResponse;
		InstanciaDTO instanciaDTO = new InstanciaDTO(request.getInstancia(), request.getIp(), Constantes.OPERACION_ALL,
				request.getFiid(), request.getCanal(), request.getNotificationUrl());
		try {
			prepagos = prepagoService.obtenerPrepagos();
			prepagosResponse = empresaManager.convertListToPrepagosResponse(prepagos);
			registrarInstancia(instanciaDTO, Constantes.SUCCESS_OPERACION, stopWatch);
			return Response.status(Response.Status.OK.getStatusCode()).entity(prepagosResponse).type("application/json")
					.build();
		} catch (EmpresasApiException e) {
			registrarInstancia(instanciaDTO,
					Constantes.ERROR_OPERACION + "|" + e.getCodigoError() + "|" + e.getMessage(), stopWatch);
			throw e;
		}
	}

	@POST
	@Path("/prepagos/{idRubro}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna una lista de Prepagos de un Rubro", notes = "Retorna una lista de Prepagos del Rubro indicado.", response = PrepagoResponse.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = PrepagoResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerPrepagosPorRubro(@Context HttpServletRequest requestContext,
			@ApiParam(value = "Rubro", required = true) @PathParam(value = "idRubro") String idRubro,
			@ApiParam(value = "La consulta se realiza enviando el FIID, Canal e Instancia", required = true) Request request)
			throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<Prepago> prepagos;
		List<PrepagoResponse> prepagosResponse;
		InstanciaDTO instanciaDTO = new InstanciaDTO(request.getInstancia(), request.getIp(), Constantes.OPERACION_ALL,
				request.getFiid(), request.getCanal(), request.getNotificationUrl());
		try {
			prepagos = prepagoService.obtenerPrepagosPorRubro(idRubro);
			prepagosResponse = empresaManager.convertListToPrepagosResponse(prepagos);
			registrarInstancia(instanciaDTO, Constantes.SUCCESS_OPERACION, stopWatch);
			return Response.status(Response.Status.OK.getStatusCode()).entity(prepagosResponse).type("application/json")
					.build();
		} catch (EmpresasApiException e) {
			registrarInstancia(instanciaDTO,
					Constantes.ERROR_OPERACION + "|" + e.getCodigoError() + "|" + e.getMessage(), stopWatch);
			throw e;
		}
	}

	@POST
	@Path("/refresh")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Realiza el refresh de Rubros, Empresas y Prepagos", notes = "Realiza el refresh de Rubros, Empresas y Prepagos", response = EstadoDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = EstadoDTO.class),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response ejecutarRefresh(@Context HttpServletRequest requestContext,
			@ApiParam(value = "La consulta se realiza enviando el FIID, Canal e Instancia", required = true) Request request)
			throws Exception {
		EstadoDTO estadoDTO = new EstadoDTO();
		request.setIp(request.getIp());
		request.setIndicePT("0");
		estadoDTO = refreshService.refresh(request);
		empresaManager.establecerEmpresas(request);
		return Response.status(Response.Status.OK.getStatusCode()).entity(estadoDTO).type("application/json").build();
	}

	@POST
	@Path("/actualizarMemoria")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Carga en memoria la lista de empresas de la Base de Datos", notes = "Carga en memoria la lista de empresas de la Base de Datos", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = Boolean.class),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response actualizarMemoria(@Context HttpServletRequest requestContext,
			@ApiParam(value = "La consulta se realiza enviando el FIID, Canal e Instancia", required = true) Request request)
			throws Exception {
		rubroService.evictCache();
		prepagoService.evictCache();
		empresaManager.establecerEmpresas(request);
		return Response.status(Response.Status.OK.getStatusCode()).entity(true).type("application/json").build();
	}

	@GET
	@Path("/estados")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna una lista de Estados", notes = "Retorna una lista de Estados. Esta lista está acotada a 10 registros y ordenado por fecha descendiente.", response = EstadoDTO.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = EstadoDTO.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerEstados() throws Exception {
		List<EstadoDTO> estadosDTO = estadoService.obtenerEstados();
		return Response.status(Response.Status.OK.getStatusCode()).entity(estadosDTO).type("application/json").build();
	}

	@GET
	@Path("/instancias")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retorna una lista de Instancias", notes = "Retorna una lista de Instancias. Esta lista está ordenada por fecha descendiente.", response = InstanciaDTO.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = InstanciaDTO.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerInstancias() throws Exception {
		List<InstanciaDTO> instanciasDTO = estadoService.obtenerInstancias();
		return Response.status(Response.Status.OK.getStatusCode()).entity(instanciasDTO).type("application/json")
				.build();
	}

	@DELETE
	@Path("instancias/{idInstancia}")
	@ApiOperation(value = "Elimina una instancia", notes = "Elimina una instancia. Esta operación sólo debe estar habilitada para el Perfil 'Soporte de Producción'", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = Boolean.class),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response eliminarInstancia(
			@ApiParam(value = "Instancia", required = true) @PathParam(value = "idInstancia") String idInstancia)
			throws Exception {
		estadoService.eliminarInstancia(idInstancia);
		return Response.status(Response.Status.OK.getStatusCode()).entity(true).type("application/json").build();
	}

	@POST
	@Path("/notificar/{idInstancia}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Notifica una instancia", notes = "Notifica una instancia para que ésta actualice su lista en memoria", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = Boolean.class),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response notificarInstancia(
			@ApiParam(value = "Instancia", required = true) @PathParam(value = "idInstancia") String idInstancia)
			throws Exception {
		InstanciaDTO instanciaDTO = null;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			instanciaDTO = estadoService.obtenerInstancia(idInstancia);
			instanciaDTO.setOperacion(Constantes.OPERACION_ALL);
			refreshService.notificarInstancia(instanciaDTO);
			instanciaDTO.setResultado(Constantes.SUCCESS_OPERACION);
			stopWatch.stop();
			instanciaDTO.setTiempo(stopWatch.getTotalTimeMillis() + "ms");
			estadoService.actualizarInstancia(instanciaDTO);
			return Response.status(Response.Status.OK.getStatusCode()).entity(true).type("application/json").build();
		} catch (EmpresasApiException e) {
			instanciaDTO.setResultado(Constantes.ERROR_OPERACION + "|" + e.getCodigoError() + "|" + e.getMessage());
			stopWatch.stop();
			instanciaDTO.setTiempo(stopWatch.getTotalTimeMillis() + "ms");
			estadoService.actualizarInstancia(instanciaDTO);
			throw e;
		}
	}

	@PUT
	@Path("/empresas/ayuda")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Actualiza la ayuda de una empresa", notes = "Actualiza la ayuda de una empresa.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = Boolean.class),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response actualizarAyuda(@Context HttpServletRequest requestContext,
			@ApiParam("El request AyudaEmpresa a insertar o actualizar") AyudaEmpresa ayudaEmpresa) {
		ayudaEmpresaService.guardar(ayudaEmpresa);
		return Response.status(Response.Status.OK.getStatusCode()).entity(Boolean.TRUE).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@GET
	@Path("/empresas/{institucion}/{codigo}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Obtener una empresa", notes = "La consulta se realiza indicando institución y código de la empresa a buscar")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Empresa", response = EmpresaResponse.class),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response obtenerEmpresaPorInstitucionYCodigo(@Context HttpServletRequest requestContext,
			@ApiParam(name = "institucion", value = "Institucion que realiza la consulta") @PathParam(value = "institucion") String institucion,
			@ApiParam(name = "codigo", value = "Codigo de la empresa a consultar") @PathParam(value = "codigo") String codigo)
			throws Exception {
		EmpresaResponse response = empresaManager.obtenerEmpresaPorInstitucionYCodigo(institucion, codigo);
		return Response.status(Response.Status.OK.getStatusCode()).entity(response).type(MediaType.APPLICATION_JSON)
				.build();
	}

	private void registrarInstancia(InstanciaDTO instanciaDTO, String resultado, StopWatch stopWatch) {
		if (Constantes.ORIGEN_EMPRESAS_API.equals(instanciaDTO.getIdInstancia()) || instanciaDTO == null)
			return;

		try {
			instanciaDTO.setResultado(resultado);
			stopWatch.stop();
			instanciaDTO.setTiempo(stopWatch.getTotalTimeMillis() + "ms");
			estadoService.actualizarInstancia(instanciaDTO);
		} catch (EmpresasApiException e) {
		}
	}

	private UsuarioResponse convertToUsuarioResponse(Usuario usuario) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		UsuarioResponse usuarioResponse = mapper.convertValue(usuario, UsuarioResponse.class);
		usuarioResponse.getListaRoles().addAll(Arrays.asList(usuario.getRoles().split("\\|")));
		return usuarioResponse;
	}

	private String obtenerIPAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (ipAddress != null)
			ipAddress = ipAddress.split(",")[0];
		if (ipAddress == null)
			ipAddress = request.getRemoteAddr();
		if (ipAddress == null)
			ipAddress = "";
		return ipAddress.replace(",", "");
	}
}
