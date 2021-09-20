package com.banelco.empresas.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoDeudaEnLineaDTO {
	private String codigoEmpresa;
	private String descripcionAdicional;
}