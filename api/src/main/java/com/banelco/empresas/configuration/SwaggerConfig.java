package com.banelco.empresas.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Configuration
@PropertySource(value="classpath:empresas-api.properties")
public class SwaggerConfig {

	@Value("${com.banelco.empresas.swagger.host}")
	private String host;
	
	@Bean
	public ApiListingResource apiListingResourceJSON() {
		return new ApiListingResource();
	}
	
	@Bean
	public SwaggerSerializers apiDeclarationProvider() {
		return new SwaggerSerializers();
	}
	
	@Bean
	public BeanConfig beanConfig() {
		String[] schemes = {"http","http"};
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setResourcePackage("com.banelco.empresas");
		beanConfig.setVersion("2.0");
		beanConfig.setSchemes(schemes);
		beanConfig.setHost(host);
		beanConfig.setBasePath("/empresas-api/servicios");
		beanConfig.setTitle("Empresas - API Rest");
		beanConfig.setDescription("Endpoint Rest de Empresas. De uso exclusivo de Prisma Medios de Pagos S.A. NO debe ser expuesto");
		beanConfig.setScan(true);
		return beanConfig;
	}
}
