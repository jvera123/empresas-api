package com.banelco.empresas.configuration;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@Configuration
@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {
	@Bean
	public JacksonJaxbJsonProvider jacksonConfig() {
		JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper(),
				JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS);
		return jsonProvider;
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
		return mapper;
	}
}
