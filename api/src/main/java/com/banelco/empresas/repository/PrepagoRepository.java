package com.banelco.empresas.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Prepago;

@Repository
public interface PrepagoRepository extends JpaRepository<Prepago, Long>
{
	@Cacheable("prepagos")
	List<Prepago> findAll();
	
	@Query("FROM Prepago WHERE id_rubro = :rubroId")
	List<Prepago> obtenerPrepagosPorRubro(@Param("rubroId") String rubroId) throws EmpresasApiException;
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Prepago WHERE id_rubro = :rubroId")
	void eliminarPrepagosDeUnRubro(@Param("rubroId") String rubroId) throws EmpresasApiException;
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Prepago WHERE idRefresh != :idRefresh")
	void eliminarPrepagos(@Param("idRefresh") String idRefresh) throws EmpresasApiException;
}
