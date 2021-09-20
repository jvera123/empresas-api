package com.banelco.empresas.rest.response.empresas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrepagoResponse 
{
	private String codigo;
	private RubroResponse rubro;
	private String descripcion;
	private int datosTicket;
	private int nroLeyenda;
	private int orden;
	private String montos;
}
