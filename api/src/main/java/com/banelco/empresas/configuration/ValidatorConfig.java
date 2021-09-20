package com.banelco.empresas.configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig 
{
	@Bean
	public Validator validator() 
	{
		HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class).configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		return factory.getValidator();
	}
}
