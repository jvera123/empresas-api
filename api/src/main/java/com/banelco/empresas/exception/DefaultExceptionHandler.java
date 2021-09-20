package com.banelco.empresas.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.banelco.empresas.model.dto.ErrorCompanyDTO;
import com.banelco.empresas.model.dto.ErrorDTO;
import com.banelco.empresas.util.log.LoggingInInterceptor;

@Provider
@Component
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {
	private static final Log logger = LogFactory.getLog(DefaultExceptionHandler.class);

	@Autowired
	LoggingInInterceptor interceptor;

	public Response toResponse(Exception e) {
		Response response = null;
		if (e instanceof EmpresasApiException) {
			EmpresasApiException eaEx = (EmpresasApiException) e;
			ErrorDTO error = new ErrorDTO(eaEx.getCodigoError(), eaEx.getMessage());
			response = Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(error)
					.type(MediaType.APPLICATION_JSON).build();
		} else if(e instanceof CompanyApiException){
			CompanyApiException caEx = (CompanyApiException)e;

			response = Response.status(Response.Status.BAD_REQUEST.getStatusCode())
					.entity(caEx.getError())
					.type(MediaType.APPLICATION_JSON).build();
		}else {
			logger.error(e.getClass().getSimpleName() + "|" + e.getMessage(), e);
			ErrorDTO error = new ErrorDTO("EA99", "Error genérico en Empresas-API");
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(error)
					.type(MediaType.APPLICATION_JSON).build();
		}
		interceptor.postProcess(response);
		return response;
	}
}
