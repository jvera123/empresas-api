package com.banelco.empresas.util.validation;

public class ValidateUtil {

	/*
	 * Patrones genericos V2
	 */
	public static final String REGEX_AGENT_ID = "[A-Z0-9]{3,4}";
	public static final String REGEX_CONSUMER_ID = "[A-Z0-9]{3,4}";
	public static final String REGEX_CHANNEL = "[A-Z]{1}";
	public static final String REGEX_IP_ADDRESS = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	public static final String REGEX_TERMINAL = "\\S{5,20}";
	public static final String REGEX_ORIGIN = "\\S{1,16}";
	public static final String REGEX_TIMESTAMP = "\\S{0,14}";
	public static final String REGEX_DOCUMENT_NUMBER = "\\d{5,12}";
	public static final String REGEX_DOCUMENT_TYPE = "(DNI|CI|PAS|LC|LE)";

}
