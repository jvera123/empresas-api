package com.banelco.empresas.rest.response.empresas.v2;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompaniesSubItemResponse implements Comparable<CompaniesSubItemResponse>{
	
	
	
	@JsonAlias({"code", "code"})
	@JsonProperty("code")
	private String code;
	
	@JsonAlias({"name", "name"})
	@JsonProperty("name")
	private String name;
	
	@JsonAlias({"orden", "orden"})
	@JsonProperty("orden")
	private int orden;
	
	@JsonAlias({"type", "type"})
	@JsonProperty("type")
	private String type;

	/**
	 * El tipo de rubro, es para saber si tiene empresas Prepago, sin Prepago o
	 * ambas
	 */
	@JsonAlias({"type_item", "typeItem"})
	@JsonProperty("type_item")
	private TipoRubro typeItem;

	@Override
	public int compareTo(CompaniesSubItemResponse o) {
		return getOrden() - o.getOrden();
		
	}
	
	@Override
	public boolean equals(Object o)
	{
		CompaniesSubItemResponse r = (CompaniesSubItemResponse)o;
		return getCode().equals(r.getCode()) && getName().equals(r.getName());
	}
}
