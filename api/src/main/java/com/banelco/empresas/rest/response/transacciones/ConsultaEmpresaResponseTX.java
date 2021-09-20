package com.banelco.empresas.rest.response.transacciones;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaEmpresaResponseTX {
	private Map<String, EmpresaResponseTX> empresasResponse = new HashMap<String, EmpresaResponseTX>();
	private String indicePT;
}
