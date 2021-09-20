package com.banelco.empresas.util.log;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiLogDataOut {
	private static final String FILED_SEPARATOR = "\n";

	private Date outDate;
	private Date inDate;
	private String url;
	private String method;
	private String headers;
	private String parameters;
	private String queryParameters;
	private String request;
	private String response;
	private int status;

	@ToString.Include
	public String toString() {
		String separator = FILED_SEPARATOR;

		StringBuilder sb = new StringBuilder("Registro");

		sb.append(separator);
		sb.append("---------------------------");
		sb.append(separator);

		sb.append("Out Date: ");
		sb.append(wrapData(outDate.toString()));
		sb.append(separator);

		sb.append("In Date: ");
		sb.append(wrapData(inDate.toString()));
		sb.append(separator);

		sb.append("URL: ");
		sb.append(wrapData(url));
		sb.append(separator);

		sb.append("Method: ");
		sb.append(wrapData(method));
		sb.append(separator);

		sb.append("Headers: ");
		sb.append(wrapData(headers));
		sb.append(separator);

		sb.append("Parameters: ");
		sb.append(wrapData(parameters));
		sb.append(separator);

		sb.append("Query Parameters: ");
		sb.append(wrapData(queryParameters));
		sb.append(separator);

		sb.append("Request: ");
		sb.append(wrapData(request));
		sb.append(separator);

		sb.append("Response: ");
		sb.append(wrapData(response));
		sb.append(separator);

		sb.append("Status: ");
		sb.append(status);
		sb.append(separator);

		sb.append("Duration: ");
		sb.append((inDate.getTime() - outDate.getTime()));
		sb.append("ms");
		sb.append(separator);

		sb.append("---------------------------");

		return sb.toString();
	}

	private String wrapData(String data) {
		return StringUtils.isBlank(data) ? "" : data;
	}
}
