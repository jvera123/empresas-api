package com.banelco.empresas.repository.ayuda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.ayudas.EmpresaInfo;

@Repository
public interface EmpresaInfoRepository extends JpaRepository<EmpresaInfo, Long>
{
	@Query("FROM EmpresaInfo WHERE codigo = :fiid")
	EmpresaInfo obtener(@Param("fiid") String fiid) throws EmpresasApiException;
}
