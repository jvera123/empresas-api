package com.banelco.empresas.service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Usuario;
import com.banelco.empresas.util.annotation.Wipe;

public interface UsuarioService
{
	public Usuario autenticar(String nombreUsuario, @Wipe String contrasena) throws EmpresasApiException;
}
