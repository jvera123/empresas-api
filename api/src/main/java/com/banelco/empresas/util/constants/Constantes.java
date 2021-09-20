package com.banelco.empresas.util.constants;

import com.banelco.empresas.rest.response.empresas.v2.CurrencyCode;

public interface Constantes
{
	public static final String ORIGEN_EMPRESAS_API = "EMPRESAS-API";
	public static final String OPERACION_ALL = "ALL";

	public static final String ERROR_COMANDO_SELECT_RUBROS = "Error al ejecutar el comando SELECT para obtener los rubros. Lista vacía o null. Pruebe primero ejecutando el REFRESH.";
	public static final String ERROR_COMANDO_DELETE_RUBROS = "Error al ejecutar el comando DELETE FROM Rubro";
	public static final String ERROR_COMANDO_PERSIST_RUBROS = "Error al ejecutar el comando PERSIST/FLUSH para los Rubros";

	public static final String ERROR_COMANDO_PERSIST_EMPRESAS = "Error al ejecutar el comando PERSIST/FLUSH para las Empresas";
	public static final String ERROR_COMANDO_SELECT_EMPRESAS = "Error al ejecutar el comando SELECT para obtener las empresas. Lista vacía o null. Pruebe primero ejecutando el REFRESH.";

	public static final String ERROR_COMANDO_SELECT_PREPAGOS = "Error al ejecutar el comando SELECT para obtener las Empresas de Prepago. Lista vacía o null. Pruebe primero ejecutando el REFRESH.";
	public static final String ERROR_COMANDO_PERSIST_PREPAGOS = "Error al ejecutar el comando PERSIST/FLUSH para las Empresas de Prepago";

	public static final String ERROR_COMANDO_SELECT_INSTANCIAS = "Error al ejecutar el comando SELECT para obtener los registros. Lista vacía o null.";
	public static final String ERROR_COMANDO_SELECT_INSTANCIA = "Error al ejecutar el comando SELECT para obtener el registro. Registro vacío o null.";
	public static final String ERROR_COMANDO_SELECT_ESTADOS = "Error al ejecutar el comando SELECT para obtener los estados. Lista vacía o null.";

	public static final String SUCCESS_OPERACION = "OK";
	public static final String ERROR_OPERACION = "ERROR";
	public static final String SUCCESS_CODIGO_GENERICO = "200";
	public static final String ERROR_CODIGO_GENERICO = "1000";
	
	public static final Integer MONEDA_PESO_TANDEM = 1;
	public static final Integer MONEDA_DOLAR_TANDEM = 2;
	
	public static final Integer PESOS = 32;
	public static final Integer DOLARES = 840;

	
	public static final String RUBRO_IEDV = "IEDV";
	public static final String RUBRO_IEDU = "IEDU";
	public static final String RUBRO_DONACIONES = "DNCS";
	public static final String RUBRO_RECARGAS = "RCEL";
	public static final String[] RUBROS_DOMESTICAS = new String[] { "DOME", "DOJU", "DOAC" };
	public static final String[] EMPRESAS_IMPORTE_FIJO = new String[] {  "FMYU", "QFFS" };
	
	public static final String CURRENCY_CODE_ARS_ID = "ARS";
	public static final String CURRENCY_CODE_ARS_NAME = "Pesos";
	public static final String CURRENCY_CODE_USD_ID = "USD";
	public static final String CURRENCY_CODE_USD_NAME = "Dólares";
	
	public static final String PAYMENT_TYPE_ID_BANK_ACCOUNT = "bank_account";
	public static final String PAYMENT_TYPE_ID_CREDIT_CARD = "credit_card";
	public static final String PAYMENT_TYPE_ID_DEBID_CARD = "debit_card";
	
	public static final String DEMO = "DEMO";
	public static final String EFEC = "EFEC";
	public static final String TPGO = "TPGO";
	
	public static final String INSTITUTION_NOT_FOUND_ERROR_MESSAGE = "Consumer id value is not valid";
	public static final String INSTITUTION_NOT_FOUND_ERROR_CODE = "EA31";
}
