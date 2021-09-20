package com.banelco.empresas.util.converters;

public class MontoConverter
{
	public static String convertirMontoMax(double montoMax)
	{
		String retorno = "";
		if (montoMax <= 0)
		{
			retorno = "000000";
		}
		else
		{
			retorno = String.format("%06d", (int) montoMax);
		}
		return retorno;
	}

	public static String convertirMontoMin(double montoMin)
	{
		String retorno = "";
		if (montoMin > 0)
		{
			retorno = String.format("%06d", (int) montoMin);

		}
		else
		{
			retorno = "000000";
		}
		return retorno;
	}
}
