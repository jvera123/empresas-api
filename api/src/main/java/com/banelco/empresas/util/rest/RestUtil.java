package com.banelco.empresas.util.rest;

import org.jboss.resteasy.client.ClientResponse;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.ErrorDTO;

public class RestUtil {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <DtoT> DtoT obtenerDTO(ClientResponse clientResponse, Class<DtoT> claseDto, String banco, String operation)
			throws EmpresasApiException {

		DtoT response = null;
		if (clientResponse.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
			response = claseDto.cast(clientResponse.getEntity(claseDto));
		} else {
			ErrorDTO errorDTO = (ErrorDTO) clientResponse.getEntity(ErrorDTO.class);
			throw new EmpresasApiException(errorDTO.getCodigo(), banco);
		}
		return response;

	}
	

}
