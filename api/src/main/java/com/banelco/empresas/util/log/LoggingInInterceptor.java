package com.banelco.empresas.util.log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.banelco.empresas.util.security.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Component
@ServerInterceptor
public class LoggingInInterceptor implements PreProcessInterceptor, PostProcessInterceptor {
	
	private static final int maxJsonEntries = 24;
	private static final int maxEntitySize = maxJsonEntries * 1024;
	private static final Logger log = LoggerFactory.getLogger(LoggingInInterceptor.class);
	
	@Context
	HttpServletRequest servletRequest;
	
	@SuppressWarnings("static-access")
	public ServerResponse preProcess(HttpRequest request, ResourceMethod method) {
		if (!request.getUri().getPath().contains("swagger")) {
			ApiLogDataIn apiLogDataIn = new ApiLogDataIn();
			apiLogDataIn.setInDate(new Date());

			apiLogDataIn.setHeaders(request.getHttpHeaders().getRequestHeaders().values().toString());
			apiLogDataIn.setPath(request.getUri().getPath());
			apiLogDataIn.setMethod(request.getHttpMethod());
			apiLogDataIn.setParameters(request.getUri().getPathParameters().toString());
			apiLogDataIn.setQueryParameters(request.getUri().getQueryParameters().toString());

			if (request.getInputStream() != null) {
				StringBuilder b = new StringBuilder();
				request.setInputStream(logInboundEntity(b, request.getInputStream()));
				apiLogDataIn.setRequest(SecurityUtil.wipeAndProtectData(b.toString()));
			}

			ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();
			factory.pushContext(ApiLogDataIn.class, apiLogDataIn);
		}
		return null;
	}
	
	public void postProcess(ServerResponse response) {
		postProcess((Response) response);
	}
	
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void postProcess(Response response) {
		ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();
		ApiLogDataIn apiLogDataIn = factory.popContextData(ApiLogDataIn.class);

		if (apiLogDataIn != null) {
			apiLogDataIn.setOutDate(new Date());
			apiLogDataIn.setStatus(response.getStatus());
			if (response.getEntity() != null) {
				Object o = response.getEntity();
				if ((o instanceof List && ((List) o).size() <= maxJsonEntries) || !(o instanceof List)) {
					try {
						ObjectMapper mapper = new ObjectMapper();
						String json;
						json = new String(mapper.writer().writeValueAsBytes(response.getEntity()));
						apiLogDataIn.setResponse(SecurityUtil.wipeAndProtectData(json));
					} catch (JsonProcessingException e) {
					}
				} else {
					apiLogDataIn.setResponse("");
				}
			}
			log.info(apiLogDataIn.toString());
		}
	}
	
	private InputStream logInboundEntity(StringBuilder b, InputStream stream) {
		if (!stream.markSupported()) {
			stream = new BufferedInputStream(stream);
		}
		stream.mark(maxEntitySize + 1);
		byte[] entity = new byte[maxEntitySize + 1];
		int entitySize;
		try {
			entitySize = stream.read(entity);
			if (entitySize > 0) {
				b.append(new String(entity, 0, Math.min(entitySize, maxEntitySize)));
				if (entitySize > maxEntitySize) {
					b.append("...more...");
				}
				b.append('\n');
				stream.reset();
				return stream;
			}
		} catch (IOException e) {
			return null;
		}
		return null;
	}
}
