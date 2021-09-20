package com.banelco.empresas.model.mapper;

import java.util.ArrayList;
import java.util.List;

import com.banelco.empresas.model.entity.Rubro;
import com.banelco.empresas.rest.response.empresas.v2.CategoryResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CategoryMapper {
	
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
	
	public static CategoryResponse map(Rubro rubro) {
		CategoryResponse category = mapper.convertValue(rubro, CategoryResponse.class);
		category.setId(rubro.getIdEmbeddedIdRubro().getIdRubro());
		category.setType(rubro.getIdEmbeddedIdRubro().getTipo());
		return category;
	}
	
	public static List<CategoryResponse> map(List<Rubro> rubros) {
		List<CategoryResponse> categories = new ArrayList<CategoryResponse>();
		for (Rubro rubro : rubros) {
			categories.add(map(rubro));
		}
		return categories;
	}
}
