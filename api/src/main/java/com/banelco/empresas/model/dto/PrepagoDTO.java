package com.banelco.empresas.model.dto;

import java.util.ArrayList;

import com.banelco.empresas.model.entity.Rubro;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrepagoDTO {
	private String codigo;
	private String rubroId;
	private String desc;
	private int datosTicket;
	private int nroLeyenda;
	private int orden;
	private String montos;
	private Rubro rubro;

	@SuppressWarnings("rawtypes")
	public String toStringMonto(ArrayList montos) {
		String retorno = "";
		for (Object object : montos) {
			retorno = object.toString();
		}
		return retorno;
	}

}
