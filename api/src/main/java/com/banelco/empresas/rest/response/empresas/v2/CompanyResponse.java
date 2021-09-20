package com.banelco.empresas.rest.response.empresas.v2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompanyResponse {
	@JsonAlias({ "id", "codigo" })
	@JsonProperty("id")
	private String id;

	@JsonAlias({ "type", "tipoEmpresa" })
	@JsonProperty("type")
	private String type;

	@JsonAlias({ "name", "nombre" })
	@JsonProperty("name")
	private String name;

	@JsonAlias({ "cuit", "cuit" })
	@JsonProperty("cuit")
	private String cuit;

	@JsonAlias({ "order", "orden" })
	@JsonProperty("order")
	private int order = 0;

	@JsonAlias({ "read_only", "soloConsultas" })
	@JsonProperty("read_only")
	private boolean readOnly;

  @JsonAlias({"payment_type", "tipoPago"})
  @JsonProperty("payment_type")
  private Integer paymentType;

  @JsonAlias({"amount_type", "importePermitido"})
  @JsonProperty("amount_type")
  private Integer amountType;

  @JsonAlias({"recharge_type", "tipoRecarga"})
  @JsonProperty("recharge_type")
  private String rechargeType;

  private String pictureUrl;

	@JsonAlias({ "has_annual_billing", "permiteDobleFacturacion" })
	@JsonProperty("has_annual_billing")
	private boolean hasAnnualBilling;

	private List<RecurrentPaymentType> recurrentPaymentTypes;

	private List<Double> allowedAmounts;

	private List<CurrencyCode> currencyCodes;

	private List<PaymentMethod> paymentMethods;

	private Identification identification;

	@JsonAlias({ "category", "rubro" })
	@JsonProperty("category")
	private CategoryResponse category;
}
