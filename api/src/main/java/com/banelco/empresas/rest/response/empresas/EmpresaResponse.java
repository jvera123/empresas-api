package com.banelco.empresas.rest.response.empresas;

import com.banelco.empresas.model.dto.ConfigRecargaDTO;
import com.banelco.empresas.model.dto.InfoDeudaEnLineaDTO;
import com.banelco.empresas.model.dto.InfoRecargaDTO;
import com.banelco.empresas.model.dto.InfoVidrieraDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmpresaResponse
{
	private String codigo;
	private int orden = 0;
	private String tipoEmpresa;
	private String nombre;
	private String cuit;
	private String tituloIdentificacion;
	private Integer tipoAdhesion;
	private Integer tipoPago;
	private Integer codigoMoneda;
	private String datoAdicional = "";
	private boolean soloConsultas = false;
	private boolean permiteUsd = true;
	private boolean permiteEBPP = false;
	private Integer importePermitido;
	
	private boolean permitePagosRecurrentes = false;
	private boolean permiteDobleFacturacion = false;
	private boolean permitePagosTCV = false;
	private boolean permitePagosTCM = false;
	private boolean permitePagosTCA = false;
	private boolean permitePagosTDV = false;
	private boolean permitePagosTDM = false;
	private double montoMinimoTC;
	private double montoMaximoTC;
	private Integer cantidadVencimientos;
	private Integer longitudClaveMinima;
	private Integer longitudClaveMaxima;
	private RubroResponse rubro;
	private ConfigRecargaDTO configRecarga;
	private String ayuda;
	private InfoVidrieraDTO infoVidriera;
	private InfoRecargaDTO infoRecarga;
	
	// No van al API Manager
	private String fiid = "";
	private String leyendaSubsidios = null;
	private String leyendaSubsidiosPA = null;
	private InfoDeudaEnLineaDTO infoDeudaEnLinea;
	private boolean soloPmc;
	private String displayName;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmpresaResponse other = (EmpresaResponse) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
}
