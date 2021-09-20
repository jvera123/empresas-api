package com.banelco.empresas.model.ayudas;

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
@Table(name = "EmpresasInfo")
public class EmpresaInfo
{
	@Id
	@Column(name = "EmpresaID")
	private String codigo;
	
	@Column(name = "CantVtosAyuda")
	private Integer cantidadVencimientos;
	
	@Column(name = "LongMinClave")
	private Integer longitudClaveMinima;
	
	@Column(name = "LongMaxClave")
	private Integer longitudClaveMaxima;
	
	@Column(name = "CUIT")
	private String cuit;
}
