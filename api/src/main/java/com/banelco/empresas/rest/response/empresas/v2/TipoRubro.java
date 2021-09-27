package com.banelco.empresas.rest.response.empresas.v2;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TipoRubro {
	
	@JsonAlias({"prepago", "prepago"})
	@JsonProperty("prepago")
	private Boolean prepago;
	
	@JsonAlias({"empresa_comun", "empresaComun"})
	@JsonProperty("empresa_comun")
	private Boolean empresaComun;
	
	@JsonAlias({"consulta", "consulta"})
	@JsonProperty("consulta")
	private Boolean consulta;
	
	@JsonAlias({"pago", "pago"})
	@JsonProperty("pago")
	private Boolean pago;

	public TipoRubro()
	{
		this.prepago = false;
		this.empresaComun = false;
		this.consulta = true;
		this.pago = true;
	}
	
	public void unirTipoRubro(TipoRubro tipoRubro)
	{
		this.prepago = this.prepago || tipoRubro.getPrepago();
		this.empresaComun = this.empresaComun || tipoRubro.getEmpresaComun();
		this.consulta = this.consulta && tipoRubro.getConsulta();
		this.pago = this.pago || tipoRubro.getPago();
	}


}
