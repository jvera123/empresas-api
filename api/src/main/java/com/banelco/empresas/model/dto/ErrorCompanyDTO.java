package com.banelco.empresas.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorCompanyDTO {
	private String code;
	private String message;
}
