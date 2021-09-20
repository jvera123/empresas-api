package com.banelco.empresas.model.dto;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstanciaDTO {
	private String idInstancia;
	private String ip;
	private String operacion;
	private String fiid;
	private String canal;
	private String fecha;
	private String resultado;
	private boolean actualizar = false;
	private String tiempo;
	private boolean error;
	private String notificationUrl;

	public InstanciaDTO(String idInstancia, String ip, String operacion, String fiid, String canal,
			String notificationUrl) {
		this.idInstancia = idInstancia;
		this.ip = ip;
		this.operacion = operacion;
		this.fiid = fiid;
		this.canal = canal;
		this.notificationUrl = notificationUrl;
	}

	public String getPmctasUrl() {
		return "http://" + this.ip + ":8080/pmctas/";
	}

	public String getMobileUrl() {
		return "http://" + this.ip + ":8080/mobile/";
	}

	public String getServicesUrl() {
		return "http://" + this.ip + ":8080/services/";
	}

	public String getMkpUrl() {
		if(StringUtils.isBlank(this.notificationUrl)) {
			return "http://" + this.ip + ":8080/marketplace-middleware/internal/rest/v1/actualizarListas";
		} else {
			return this.notificationUrl;
		}
	}
}
