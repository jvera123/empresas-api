package com.banelco.empresas.rest.response.empresas.v2;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryResponse {
	
	@JsonAlias({"id", "idRubro"})
	@JsonProperty("id")
	private String id;
	
	@JsonAlias({"type", "tipo"})
	@JsonProperty("type")
	private String type;
	
	@JsonAlias({"name", "nombre"})
	@JsonProperty("name")
	private String name;
	
	@JsonAlias({"order", "orden"})
	@JsonProperty("order")
	private Integer order;
	
}
