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
import com.banelco.empresas.model.entity.Rubro;
import com.banelco.empresas.model.entity.RubroId;

@Repository
public interface RubroRepository extends JpaRepository<Rubro, RubroId>
{
	@Query("FROM Rubro WHERE id_rubro = :rubroId AND fiid = :fiid")
	List<Rubro> obtenerRubro(@Param("rubroId") String rubroId,@Param("fiid") String institucion) throws EmpresasApiException;
	
	@Query("FROM Rubro WHERE idRefresh != :idRefresh AND fiid = :fiid")
	List<Rubro> obtenerRubrosNoPresentes(@Param("idRefresh") String idRefresh,@Param("fiid") String institucion) throws EmpresasApiException;
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Rubro WHERE idRefresh != :idRefresh AND fiid = :fiid")
	void eliminarRubros(@Param("idRefresh") String idRefresh,@Param("fiid") String institucion) throws EmpresasApiException;

	@Query("FROM Rubro WHERE tipo != 0 AND fiid = :fiid")
	List<Rubro> obtenerSubRubros(@Param("fiid") String institucion);
	
	@Query("FROM Rubro WHERE fiid = :fiid")
	@Cacheable("rubros")
	List<Rubro> obtenerRubroPorInstitucion(@Param("fiid") String institucion) throws EmpresasApiException;
	
	@Query("FROM Rubro r WHERE r.idEmbeddedIdRubro.idRubro NOT IN (SELECT e.rubro.idEmbeddedIdRubro.idRubro FROM Empresa e) AND r.idEmbeddedIdRubro.fiid = 'TPGO'")
	List<Rubro> obtenerRubrosSinEmpresas() throws EmpresasApiException;
	
	List<Rubro> findByIdEmbeddedIdRubroFiid(String fiid) throws EmpresasApiException;
}
