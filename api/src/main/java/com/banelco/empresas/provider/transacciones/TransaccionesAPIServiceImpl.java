package com.banelco.empresas.provider.transacciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.provider.RestClientFactory;
import com.banelco.empresas.provider.transacciones.client.TransaccionesRestClient;
import com.banelco.empresas.provider.transacciones.response.ErrorResponse;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.response.transacciones.ConsultaEmpresaResponseTX;
import com.banelco.empresas.rest.response.transacciones.EmpresaResponseTX;
import com.banelco.empresas.rest.response.transacciones.PrepagoResponseTX;
import com.banelco.empresas.rest.response.transacciones.RubroResponseTX;
import com.banelco.empresas.service.TransaccionesAPIService;

@Service
@PropertySource("classpath:empresas-api.properties")
public class TransaccionesAPIServiceImpl implements TransaccionesAPIService {
	private static final Logger logger = LoggerFactory.getLogger(TransaccionesAPIServiceImpl.class);

	@Autowired
	private Environment env;

	private TransaccionesRestClient client;

	private TransaccionesRestClient getClient() {
		if (client == null) {
			client = RestClientFactory.create(env.getProperty("empresas.transaccionesB24.url"),
					env.getProperty("empresas.transaccionesB24.connectiontimeout", Integer.class),
					env.getProperty("empresas.transaccionesB24.receivetimeout", Integer.class),
					TransaccionesRestClient.class);
		}
		return client;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<RubroResponseTX> obtenerRubros(Request request) throws EmpresasApiException {
		try {
			ClientResponse clientResponse = getClient().obtenerRubros(request);
			if (clientResponse.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
				List<RubroResponseTX> response = (List<RubroResponseTX>) clientResponse
						.getEntity(new GenericType<List<RubroResponseTX>>() {
						});
				logger.info("Se obtuvieron " + response.size() + " rubros en total");
				return response;
			} else {
				ErrorResponse errorResponse = (ErrorResponse) clientResponse.getEntity(ErrorResponse.class);
				throw new EmpresasApiException(errorResponse.getCodigo(),
						errorResponse.getMensaje() != null ? errorResponse.getMensaje()
								: "Error en consulta de Rubros hacia Transacciones");
			}
		} catch (Exception e) {
			throw new EmpresasApiException(e.getMessage(), "RUBROS_API_ERROR_01");
		} finally {
			//RestClientFactory.close();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<EmpresaResponseTX> obtenerEmpresas(Request request) throws EmpresasApiException {
		try {
			List<EmpresaResponseTX> response = new ArrayList<EmpresaResponseTX>();
			int viajes = 0;
			do {
				ClientResponse clientResponse = getClient().obtenerEmpresas(request);
				if (clientResponse.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
					ConsultaEmpresaResponseTX consultaEmpresas = (ConsultaEmpresaResponseTX) clientResponse
							.getEntity(ConsultaEmpresaResponseTX.class);
					response.addAll(consultaEmpresas.getEmpresasResponse().values());
					request.setIndicePT(consultaEmpresas.getIndicePT());
					viajes++;
					if (viajes % 10 == 0) {
						logger.info("Se obtuvieron " + response.size() + " empresas");
					}
				} else {
					ErrorResponse errorResponse = (ErrorResponse) clientResponse.getEntity(ErrorResponse.class);
					throw new EmpresasApiException(errorResponse.getCodigo(),
							errorResponse.getMensaje() != null ? errorResponse.getMensaje()
									: "Error en consulta de Empresas hacia Transacciones");
				}
			} while (!request.getIndicePT().equals("99"));
			return response;
		} catch (Exception e) {
			throw new EmpresasApiException(e.getMessage(), "EMPRESAS_API_ERROR_01");
		} finally {
			//RestClientFactory.close();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<PrepagoResponseTX> obtenerPrepagos(Request request) throws EmpresasApiException {
		try {
			ClientResponse clientResponse = getClient().obtenerPrepagos(request);
			if (clientResponse.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
				Map<String, PrepagoResponseTX> prepagosResponse = (Map<String, PrepagoResponseTX>) clientResponse
						.getEntity(new GenericType<Map<String, PrepagoResponseTX>>() {
						});
				List<PrepagoResponseTX> response = new ArrayList<PrepagoResponseTX>();
				for (Map.Entry prepago : prepagosResponse.entrySet()) {
					response.add((PrepagoResponseTX) prepago.getValue());
				}
				logger.info("Se obtuvieron " + response.size() + " prepagos en total");
				return response;
			} else {
				ErrorResponse errorResponse = (ErrorResponse) clientResponse.getEntity(ErrorResponse.class);
				throw new EmpresasApiException(errorResponse.getCodigo(),
						errorResponse.getMensaje() != null ? errorResponse.getMensaje()
								: "Error en consulta de prepagos hacia Transacciones");
			}
		} catch (Exception e) {
			throw new EmpresasApiException(e.getMessage(), "PREPAGOS_API_ERROR_01");
		} finally {
			// RestClientFactory.close();
		}
	}
}
