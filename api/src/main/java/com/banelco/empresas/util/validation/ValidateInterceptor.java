package com.banelco.empresas.util.validation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.dto.ErrorDTO;
import com.banelco.empresas.service.MensajeErrorService;

@Aspect
@Component
public class ValidateInterceptor {
	private static final Log logger = LogFactory.getLog(ValidateInterceptor.class);

	@Autowired
	private Validator validator;

	@Autowired
	private MensajeErrorService errorMessageService;

	@Before("@annotation(com.banelco.empresas.util.validation.Validate)")
	public void validarRequest(JoinPoint joinPointMetodo) throws EmpresasApiException {

		Set<ConstraintViolation<Object>> validationErrors = new HashSet<ConstraintViolation<Object>>();

		for (Object arg : joinPointMetodo.getArgs()) {
			validationErrors.addAll(validator.validate(arg));
		}

		if (!validationErrors.isEmpty()) {
			if (validationErrors.iterator() != null && validationErrors.iterator().next() != null) {
				ErrorDTO errorDTO = errorMessageService.getError(validationErrors.iterator().next().getMessage());
				throw new EmpresasApiException(errorDTO.getMensaje(), errorDTO.getCodigo());
			} else {
				logger.warn("Lista de validaciones falladas no está vacía, pero no tiene iterator válido.");
				ErrorDTO errorDTO = errorMessageService.getErrorGenerico();
				throw new EmpresasApiException(errorDTO.getMensaje(), errorDTO.getCodigo());
			}
		}
	}
}
