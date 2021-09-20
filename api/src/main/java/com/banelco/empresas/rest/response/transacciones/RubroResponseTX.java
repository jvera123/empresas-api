package com.banelco.empresas.rest.response.transacciones;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RubroResponseTX {
	private String id;
	private String desc;
	private String tipo;
	private int orden;
}
