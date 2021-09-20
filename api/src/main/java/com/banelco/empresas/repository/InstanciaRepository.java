package com.banelco.empresas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Instancia;

@Repository
public interface InstanciaRepository extends JpaRepository<Instancia, Long>
{
	@Query("FROM Instancia WHERE id_instancia = :idInstancia AND operacion = 'ALL'")
	Instancia obtenerInstancia(@Param("idInstancia") String idInstancia) throws EmpresasApiException;
	
	@Query("FROM Instancia WHERE id_instancia = :idInstancia")
	List<Instancia> obtenerInstanciaAEliminar(@Param("idInstancia") String idInstancia) throws EmpresasApiException;
}
