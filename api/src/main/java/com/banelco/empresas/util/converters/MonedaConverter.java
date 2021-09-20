package com.banelco.empresas.util.converters;

import com.banelco.empresas.util.constants.Constantes;

public class MonedaConverter
{

	public static String convert(Integer valor)
	{
		String moneda = "0";
		if (valor.equals(Constantes.MONEDA_PESO_TANDEM) || valor.equals(Constantes.PESOS))
		{
			moneda = Constantes.PESOS.toString();
		}
		else if (valor.equals(Constantes.MONEDA_DOLAR_TANDEM) || valor.equals(Constantes.DOLARES))
		{
			moneda = Constantes.DOLARES.toString();
		}
		return moneda;
	}
}
