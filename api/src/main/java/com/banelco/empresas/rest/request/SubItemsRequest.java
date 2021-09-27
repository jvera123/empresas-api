package com.banelco.empresas.rest.request;

import com.banelco.empresas.model.dto.CategoryDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubItemsRequest {
	
	@JsonProperty("fiid")
	@JsonAlias({"fiid","agent_id"})
	private String fiid;

	@JsonProperty("category")
	@JsonAlias({"category","category"})
	private CategoryDTO category;

	
}
