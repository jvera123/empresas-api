package com.banelco.empresas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{
	@Query("FROM Usuario WHERE nombreUsuario = :nombreUsuario")
	Usuario obtenerUsuario(@Param("nombreUsuario") String nombreUsuario) throws EmpresasApiException;
}
