package com.banelco.empresas.rest.response.transacciones;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrepagoResponseTX {
	private String id;
	private String rubroId;
	private String desc;
	private int datosTicket;
	private int nroLeyenda;
	private int orden;
	@SuppressWarnings("rawtypes")
	private ArrayList montos = new ArrayList();
}
