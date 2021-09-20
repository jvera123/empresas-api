package com.banelco.empresas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.model.entity.ConfigRecarga;
import com.banelco.empresas.model.entity.EmpresaID;

@Repository
public interface ConfigRecargaRepository extends JpaRepository<ConfigRecarga, EmpresaID>
{
	ConfigRecarga findByIdEmbeddedIdEmpresaFiidAndIdEmbeddedIdEmpresaCodigo(String fiid, String codigo) throws EmpresasApiException;
}
