package com.banelco.empresas.rest.response.usuario;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioResponse {
	private String nombreUsuario;
	private String nombre;
	private List<String> listaRoles = new ArrayList<String>();
}
