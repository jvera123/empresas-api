package com.banelco.empresas.rest.response.empresas.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Identification {
	private String dataLabel;
	private String additionalDataLabel;
	private int minLength;
	private int maxLength;
	private String help;
}
