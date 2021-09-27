package com.banelco.empresas.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

	@JsonAlias({"name", "name"})
	@JsonProperty("name")
	private String name;
	

}
