package com.banelco.empresas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.Empresa;
import com.banelco.empresas.model.entity.EmpresaID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, EmpresaID>
{
	@Query("FROM Empresa WHERE id_rubro = :rubroId AND fiid = :fiid")
	List<Empresa> obtenerEmpresasPorRubro(@Param("rubroId") String rubroId,@Param("fiid") String fiid) throws EmpresasApiException;
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Empresa WHERE id_rubro = :rubroId AND fiid = :fiid")
	void eliminarEmpresasDeUnRubro(@Param("rubroId") String rubroId,@Param("fiid") String fiid) throws EmpresasApiException;
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Empresa WHERE idRefresh != :idRefresh AND fiid = :fiid")
	void eliminarEmpresas(@Param("idRefresh") String idRefresh,@Param("fiid") String fiid) throws EmpresasApiException;
	
	@Deprecated
	@Query(nativeQuery = true, value="SELECT * FROM Empresa e WHERE e.id_rubro = :rubroId AND e.fiid = :fiid")
	List<Empresa> obtenerEmpresasPorRubroInstitucion(@Param("rubroId") String rubroId,@Param("fiid") String institucion) throws EmpresasApiException;
	
	@Query("FROM Empresa WHERE fiid = :fiid ")
	List<Empresa> obtenerEmpresasPorInstitucion(@Param("fiid") String institucion) throws EmpresasApiException;
	
	List<Empresa> findByIdEmbeddedIdEmpresaFiidAndRubroIdEmbeddedIdRubroIdRubroAndRubroIdEmbeddedIdRubroFiid(String fiid,String idRubro,String fiidRubro) throws EmpresasApiException;
}
