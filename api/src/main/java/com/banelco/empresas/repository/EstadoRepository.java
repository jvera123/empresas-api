package com.banelco.empresas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long>
{
	@Query("FROM Estado WHERE fecha = (SELECT MAX (e.fecha) FROM Estado e)")
	Estado obtenerEstado() throws EmpresasApiException;
	
	@Query("FROM Estado WHERE fecha = (SELECT MAX (e.fecha) FROM Estado e WHERE institucion = :institucion)")
	Estado obtenerEstadoPorInstitucion(@Param("institucion") String institucion) throws EmpresasApiException;
	
	@Query("FROM Estado ORDER BY fecha DESC")
	List<Estado> obtenerEstados() throws EmpresasApiException;
}