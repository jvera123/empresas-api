package com.banelco.empresas.repository.microservicios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banelco.empresas.model.microservicios.MensajeError;
import com.banelco.empresas.model.microservicios.MensajeErrorPrimaryKeyCompuesta;

public interface MensajesErrorRepository extends JpaRepository<MensajeError, MensajeErrorPrimaryKeyCompuesta> {

	MensajeError findByIdRcAndIdBancoAndIdOperacion(String rc, String banco, String operacion);

}
