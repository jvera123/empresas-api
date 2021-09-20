package com.banelco.empresas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banelco.empresas.model.entity.EmpresaDonacionInfo;

@Repository
public interface EmpresaDonacionInfoRepository extends JpaRepository<EmpresaDonacionInfo, String>{
}
