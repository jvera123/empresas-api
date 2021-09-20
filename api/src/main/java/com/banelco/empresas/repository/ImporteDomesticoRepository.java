package com.banelco.empresas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banelco.empresas.model.entity.ImporteDomestico;

@Repository
public interface ImporteDomesticoRepository extends JpaRepository<ImporteDomestico, String>
{

}
