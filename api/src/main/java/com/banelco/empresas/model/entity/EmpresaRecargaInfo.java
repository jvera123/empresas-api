package com.banelco.empresas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EmpresaRecargaInfo")
public class EmpresaRecargaInfo {
	
	@Id
	@Column(name = "codigo_empresa", length = 4)
	private String codigoEmpresa;
	
	@Column(name = "en_vidriera_recarga")
	private Boolean enVidrieraRecarga;
	
	@Column(name = "url_imagen")
	private String urlImagen;
}
