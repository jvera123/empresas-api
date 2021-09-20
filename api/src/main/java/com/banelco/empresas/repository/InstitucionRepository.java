package com.banelco.empresas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banelco.empresas.model.entity.AyudaEmpresa;
import com.banelco.empresas.model.entity.Institucion;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, String>{
	Institucion findByConsumerId(String Id);	
}
