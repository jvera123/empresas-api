package com.banelco.empresas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Usuario;
import com.banelco.empresas.repository.UsuarioRepository;
import com.banelco.empresas.service.UsuarioService;
import com.banelco.empresas.util.annotation.Wipe;

@Service
public class UsuarioServiceImpl implements UsuarioService
{
	@Autowired
	UsuarioRepository usuarioRepository;
	
	public Usuario autenticar(String nombreUsuario, @Wipe String contrasena) throws EmpresasApiException
	{
		Usuario usuario = usuarioRepository.obtenerUsuario(nombreUsuario);
		
		if (usuario == null || !String.valueOf(contrasena.hashCode()).equals(usuario.getContrasena()))
		{
			throw new EmpresasApiException("Los datos ingresados son incorrectos", "1000");
		}
		return usuario;
	}
}
