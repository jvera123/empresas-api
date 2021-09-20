package com.banelco.empresas.rest.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
	
	@JsonProperty("fiid")
	@JsonAlias({"fiid","agent_id"})
	private String fiid;

	@JsonProperty("canal")
	@JsonAlias({"canal","channel"})
	private String canal;

	@JsonProperty("instancia")
	@JsonAlias({"instancia","instance"})
	private String instancia;

	@JsonProperty("ip")
	@JsonAlias({"ip","ip_addres"})
	private String ip;

	@JsonProperty("indicePT")
	@JsonAlias({"indicePT","indicePt"})
	private String indicePT;

	@JsonProperty("notificationUrl")
	@JsonAlias({"notificationUrl"})
	private String notificationUrl;


	public Request(String fiid, String canal, String ip) {
		this(fiid, canal, "EMPRESAS-API", ip);
	}

	public Request(String fiid, String canal, String instancia, String ip) {
		super();
		this.fiid = fiid;
		this.canal = canal;
		this.instancia = instancia;
		this.ip = ip;
	}
}
