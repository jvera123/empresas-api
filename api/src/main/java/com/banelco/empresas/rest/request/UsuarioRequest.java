package com.banelco.empresas.rest.request;

import com.banelco.empresas.util.security.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioRequest 
{
	private String nombreUsuario;
	private String contrasena;
	
	@ToString.Include
	public String contrasena() { return SecurityUtil.wipe(); }
}
