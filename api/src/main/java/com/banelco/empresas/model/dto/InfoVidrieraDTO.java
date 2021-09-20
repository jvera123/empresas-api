package com.banelco.empresas.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoVidrieraDTO {
	private String urlImagen;
	private String urlPagina;
	private String descripcion;
	private String urlImagenDescripcion;
	private Integer orden;
}
