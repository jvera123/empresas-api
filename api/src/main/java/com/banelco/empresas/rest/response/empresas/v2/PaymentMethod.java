package com.banelco.empresas.rest.response.empresas.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethod {

	private String id;
	private String name;
	private String paymentTypeId;
	private Double minAllowedAmount;
	private Double maxAllowedAmount;
}
