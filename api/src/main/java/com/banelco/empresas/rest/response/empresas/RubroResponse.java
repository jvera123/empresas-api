package com.banelco.empresas.rest.response.empresas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RubroResponse {
	private String idRubro;
	private String tipo;
	private String nombre;
	private Integer orden;
}
