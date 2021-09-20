package com.banelco.empresas.rest.response.transacciones;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmpresaResponseTX {
	private String id;
	private String rubroId;
	private String desc;
	private String identif;
	private int tipoAdhe;
	private int importePermitido;
	private int tipoPago;
	private String tipoEmpresa = " ";
	private int moneda;
	private String datosAdic;
	private int orden;
	private boolean soloConsulta = false;
	private String fiid = "";
	private boolean permiteUsd = false;
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
}
