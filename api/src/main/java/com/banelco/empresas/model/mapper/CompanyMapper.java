package com.banelco.empresas.model.mapper;

import com.banelco.empresas.rest.response.empresas.EmpresaResponse;
import com.banelco.empresas.rest.response.empresas.v2.CompanyResponse;
import com.banelco.empresas.rest.response.empresas.v2.CurrencyCode;
import com.banelco.empresas.rest.response.empresas.v2.Identification;
import com.banelco.empresas.rest.response.empresas.v2.PaymentMethod;
import com.banelco.empresas.rest.response.empresas.v2.PaymentTypeId;
import com.banelco.empresas.rest.response.empresas.v2.RecurrentPaymentType;
import com.banelco.empresas.util.constants.Constantes;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.List;

public class CompanyMapper {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	public static CompanyResponse map(EmpresaResponse empresa) {
		CompanyResponse company = mapper.convertValue(empresa, CompanyResponse.class);
		
		String cuit = (empresa.getCuit() != null) ? empresa.getCuit() : "";
		company.setCuit(cuit);

		// recharge_type
		if (empresa.getConfigRecarga() != null && Constantes.RUBRO_RECARGAS.equalsIgnoreCase(empresa.getRubro().getIdRubro())) {
			company.setRechargeType(empresa.getConfigRecarga().getTipo());
		}

		// picture_url:
		company.setPictureUrl("");
		if (empresa.getInfoVidriera() != null && Constantes.RUBRO_DONACIONES.equals(empresa.getRubro().getIdRubro())) {
			company.setPictureUrl(empresa.getInfoVidriera().getUrlImagen());
		} else if (empresa.getInfoRecarga() != null && Constantes.RUBRO_RECARGAS.equals(empresa.getRubro().getIdRubro())) {
			company.setPictureUrl(empresa.getInfoRecarga().getUrlImagen());
		} 

		// recurrent_payment_types:
		List<RecurrentPaymentType> recurrentPaymentTypes =  new ArrayList<RecurrentPaymentType>();
		if (empresa.isPermitePagosRecurrentes()) {
			recurrentPaymentTypes.add(RecurrentPaymentType.SCHEDULED);
		}

		if (empresa.isPermitePagosRecurrentes() && empresa.getTipoPago().equals(3)) {
			recurrentPaymentTypes.add(RecurrentPaymentType.AUTOMATIC);
		}

		if (empresa.isPermitePagosRecurrentes() && !(empresa.getTipoPago().equals(3))) {
			recurrentPaymentTypes.add(RecurrentPaymentType.MONTHLY_WEEKLY);
		}
		company.setRecurrentPaymentTypes(recurrentPaymentTypes);

		// allowed_amounts:
		company.setAllowedAmounts(new ArrayList<Double>());
		if(empresa.getConfigRecarga()!=null) {
		 company.setAllowedAmounts(empresa.getConfigRecarga().getImportes());
		}

		// currency_codes:
		List<CurrencyCode> currencyCodes = new ArrayList();
		CurrencyCode currencyCodeARS = CurrencyCode.builder().id(Constantes.CURRENCY_CODE_ARS_ID)
				.name(Constantes.CURRENCY_CODE_ARS_NAME).build();
		currencyCodes.add(currencyCodeARS);

		if (empresa.isPermiteUsd()) {
			CurrencyCode currencyCodeUSD = CurrencyCode.builder().id(Constantes.CURRENCY_CODE_USD_ID)
					.name(Constantes.CURRENCY_CODE_USD_NAME).build();
			currencyCodes.add(currencyCodeUSD);
		}

		company.setCurrencyCodes(currencyCodes);

		// payment_methods:
		convertPaymentMethods(company, empresa);

		// identification
		int minLength = empresa.getLongitudClaveMinima() != null ? empresa.getLongitudClaveMinima(): 0 ;
		int maxLength = empresa.getLongitudClaveMaxima() != null ? empresa.getLongitudClaveMaxima(): 0 ;
		
		Identification identificacion = Identification.builder().dataLabel(empresa.getTituloIdentificacion())
				.additionalDataLabel(empresa.getDatoAdicional()).minLength(minLength)
				.maxLength(maxLength).help(empresa.getAyuda()).build();

		company.setIdentification(identificacion);

		return company;
	}

	public static List<CompanyResponse> map(List<EmpresaResponse> empresas) {
		List<CompanyResponse> companies = new ArrayList<CompanyResponse>();
		for (EmpresaResponse empresa : empresas) {
			if (!empresa.isSoloPmc())
				companies.add(map(empresa));
		}
		return companies;
	}

	private static void convertPaymentMethods(CompanyResponse company, EmpresaResponse empresa) {
		PaymentMethod cuentaCorriente = PaymentMethod.builder().id(PaymentTypeId.ccp.name()).name("Cuenta Corriente $")
				.paymentTypeId(Constantes.PAYMENT_TYPE_ID_BANK_ACCOUNT).build();

		PaymentMethod cajaAhorro = PaymentMethod.builder().id(PaymentTypeId.cap.name()).name("Caja de Ahorro $")
				.paymentTypeId(Constantes.PAYMENT_TYPE_ID_BANK_ACCOUNT).build();

		PaymentMethod USDcuentaCorriente = PaymentMethod.builder().id(PaymentTypeId.ccd.name())
				.name("Cuenta Corriente U$S").paymentTypeId(Constantes.PAYMENT_TYPE_ID_BANK_ACCOUNT).build();

		PaymentMethod USDcajaAhorro = PaymentMethod.builder().id(PaymentTypeId.cad.name()).name("Caja de Ahorro U$S")
				.paymentTypeId(Constantes.PAYMENT_TYPE_ID_BANK_ACCOUNT).build();

		PaymentMethod tarjetaCreditoVisa = PaymentMethod.builder().id(PaymentTypeId.tcv.name())
				.name("Tarjeta de Crédito Visa").paymentTypeId(Constantes.PAYMENT_TYPE_ID_CREDIT_CARD)
				.minAllowedAmount(empresa.getMontoMinimoTC()).maxAllowedAmount(empresa.getMontoMaximoTC()).build();

		PaymentMethod tarjetaCreditoMasterCard = PaymentMethod.builder().id(PaymentTypeId.tcm.name())
				.name("Tarjeta de Crédito MasterCard").paymentTypeId(Constantes.PAYMENT_TYPE_ID_CREDIT_CARD)
				.minAllowedAmount(empresa.getMontoMinimoTC()).maxAllowedAmount(empresa.getMontoMaximoTC()).build();

		PaymentMethod tarjetaCreditoAmex = PaymentMethod.builder().id(PaymentTypeId.tca.name())
				.name("Tarjeta de Crédito American Express").paymentTypeId(Constantes.PAYMENT_TYPE_ID_CREDIT_CARD)
				.minAllowedAmount(empresa.getMontoMinimoTC()).maxAllowedAmount(empresa.getMontoMaximoTC()).build();

		PaymentMethod tarjetaDebitoVisa = PaymentMethod.builder().id(PaymentTypeId.tdv.name())
				.name("Tarjeta de Débito Visa").paymentTypeId(Constantes.PAYMENT_TYPE_ID_DEBID_CARD).build();

		PaymentMethod tarjetaDebitoMaestro = PaymentMethod.builder().id(PaymentTypeId.tdm.name())
				.name("Tarjeta de Débito Maestro").paymentTypeId(Constantes.PAYMENT_TYPE_ID_DEBID_CARD).build();

		List<PaymentMethod> paymentMethods = new ArrayList<PaymentMethod>();
		paymentMethods.add(cuentaCorriente);
		paymentMethods.add(cajaAhorro);

		if (empresa.isPermiteUsd()) {
			paymentMethods.add(USDcuentaCorriente);
			paymentMethods.add(USDcajaAhorro);
		}

		if (empresa.isPermitePagosTCV()) {
			paymentMethods.add(tarjetaCreditoVisa);
		}

		if (empresa.isPermitePagosTCM()) {
			paymentMethods.add(tarjetaCreditoMasterCard);
		}

		if (empresa.isPermitePagosTCA()) {
			paymentMethods.add(tarjetaCreditoAmex);
		}

		if (empresa.isPermitePagosTDV()) {
			paymentMethods.add(tarjetaDebitoVisa);
		}

		if (empresa.isPermitePagosTDM()) {
			paymentMethods.add(tarjetaDebitoMaestro);
		}

		company.setPaymentMethods(paymentMethods);
	}
}
