package com.banelco.empresas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banelco.empresas.model.dto.ErrorDTO;
import com.banelco.empresas.model.microservicios.MensajeError;
import com.banelco.empresas.repository.microservicios.MensajesErrorRepository;
import com.banelco.empresas.service.MensajeErrorService;

@Service
public class MensajeErrorServiceImpl implements MensajeErrorService {

	private final String DEFAULT = "DFLT";

	private final String MENSAJE_ERROR_DEFAULT = "Error de sistema (intente repetir la operación)";

	private final String CODIGO_ERROR_DEFAULT = "EA99";

	@Autowired
	private MensajesErrorRepository mensajeErrorRepository;

	public ErrorDTO getErrorGenerico() {

		MensajeError mensajeError = mensajeErrorRepository.findByIdRcAndIdBancoAndIdOperacion(CODIGO_ERROR_DEFAULT,
				DEFAULT, DEFAULT);

		if (mensajeError != null) {
			return new ErrorDTO(mensajeError.getCodigo(), mensajeError.getMensaje());
		} else {
			return new ErrorDTO(CODIGO_ERROR_DEFAULT, MENSAJE_ERROR_DEFAULT);
		}

	}

	@Override
	public ErrorDTO getError(String responseCode, String banco, String operacion) {

		MensajeError mensajeError = mensajeErrorRepository.findByIdRcAndIdBancoAndIdOperacion(responseCode, banco,
				operacion);

		if (mensajeError != null) {
			return new ErrorDTO(mensajeError.getCodigo(), mensajeError.getMensaje());
		}

		mensajeError = mensajeErrorRepository.findByIdRcAndIdBancoAndIdOperacion(responseCode, banco, DEFAULT);

		if (mensajeError != null) {
			return new ErrorDTO(mensajeError.getCodigo(), mensajeError.getMensaje());
		}

		mensajeError = mensajeErrorRepository.findByIdRcAndIdBancoAndIdOperacion(responseCode, DEFAULT, operacion);

		if (mensajeError != null) {
			return new ErrorDTO(mensajeError.getCodigo(), mensajeError.getMensaje());
		}

		mensajeError = mensajeErrorRepository.findByIdRcAndIdBancoAndIdOperacion(responseCode, DEFAULT, DEFAULT);

		if (mensajeError != null) {
			return new ErrorDTO(mensajeError.getCodigo(), mensajeError.getMensaje());
		}

		mensajeError = mensajeErrorRepository.findByIdRcAndIdBancoAndIdOperacion(CODIGO_ERROR_DEFAULT, DEFAULT,
				DEFAULT);

		if (mensajeError != null) {
			return new ErrorDTO(responseCode, mensajeError.getMensaje());
		} else {
			return new ErrorDTO(responseCode, MENSAJE_ERROR_DEFAULT);
		}

	}

	@Override
	public ErrorDTO getError(String responseCode) {

		return getError(responseCode, DEFAULT, DEFAULT);
	}
}
