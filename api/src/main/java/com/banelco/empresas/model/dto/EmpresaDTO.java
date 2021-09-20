package com.banelco.empresas.model.dto;

import com.banelco.empresas.model.entity.Rubro;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmpresaDTO {
	private String codigo;
	private int orden = 0;
	private String tipoEmpresa;
	private String nombre;
	private boolean permiteUsd = true;
	private String leyendaSubsidios = null;
	private String leyendaSubsidiosPA = null;
	private String tituloIdentificacion;
	private Integer tipoAdhesion;
	private Integer tipoPago;
	private boolean soloConsultas = false;
	private String fiid = "";
	private Integer importePermitido;
	private Integer codigoMoneda;
	private String datoAdicional = "";
	private boolean permitePagosRecurrentes = false;
	private boolean permiteDobleFacturacion = false;
	private boolean permitePagosTCV = false;
	private boolean permitePagosTCM = false;
	private boolean permitePagosTCA = false;
	private boolean permitePagosTDV = false;
	private boolean permitePagosTDM = false;
	private double montoMinimoTC;
	private double montoMaximoTC;
	private boolean permiteEBPP = false;
	private Rubro rubro;
	private String rubroID;
	private Integer cantidadVencimientos;
	private Integer longitudClaveMinima;
	private Integer longitudClaveMaxima;
	private String cuit;
	private ConfigRecargaDTO configRecarga;
}
