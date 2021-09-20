package com.banelco.empresas.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstadoDTO {
	private String fecha;
	private int cantRubros;
	private int cantSubRubros;
	private int cantEmpresas;
	private int cantEmpresasError;
	private int cantPrepagos;
	private int cantPrepagosError;
	private String tiempo;
	private String institucion;
}
