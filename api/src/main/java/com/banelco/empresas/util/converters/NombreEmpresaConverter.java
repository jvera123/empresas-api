package com.banelco.empresas.util.converters;

public class NombreEmpresaConverter
{

	public static String convert(String desc)
	{
		char[] letras = { '!', '>', '%', '?', ':', '=', '|' };

		String[] letrasNew = {"\u00e1", "\u00e9", "\u00ed", "\u00f3", "\u00fa", "\u00f1", "\u00d1"};

		String aux = desc.toLowerCase();
		int pos = 0;
		int posEspace = -1;
		int i;

		if (desc == null || desc.trim().length() == 0)
		{
			return desc;
		}

		for (i = 0; i < letras.length; i++)
		{
			pos = aux.indexOf(letras[i]);

			while (pos != -1)
			{
				aux = aux.substring(0, pos) + letrasNew[i] + aux.substring(pos + 1);
				pos = aux.indexOf(letras[i], pos + 1);
			}

			pos = -1;
		}

		/* Inicial en Mayuscula */
		pos = aux.indexOf('#');

		while (pos != -1)
		{
			if (pos == (aux.length() - 1))
			{
				aux = aux.substring(0, pos);
			}
			else
			{
				aux = aux.substring(0, pos) + " " + aux.substring(pos + 1, pos + 2).toUpperCase() + aux.substring(pos + 2);
			}

			pos = aux.indexOf('#', pos + 1);
		}

		/* Caracter en Mayuscula */
		pos = aux.indexOf('<');

		while (pos != -1)
		{
			if (pos == (aux.length() - 1))
			{
				aux = aux.substring(0, pos);
			}
			else
			{
				aux = aux.substring(0, pos) + aux.substring(pos + 1, pos + 2).toUpperCase() + aux.substring(pos + 2);
			}

			pos = aux.indexOf('<', pos + 1);
		}

		/* Palabra en Mayuscula */
		pos = aux.indexOf('@');

		while (pos != -1)
		{
			if (pos == (aux.length() - 1))
			{
				aux = aux.substring(0, pos);
			}
			else
			{
				posEspace = aux.indexOf(' ', pos + 1);

				if (posEspace != -1)
				{
					aux = aux.substring(0, pos) + " " + aux.substring(pos + 1, posEspace).toUpperCase() + aux.substring(posEspace);
				}
				else
				{
					aux = aux.substring(0, pos) + " " + aux.substring(pos + 1).toUpperCase();
				}
			}

			pos = aux.indexOf('@', pos + 1);
		}

		/* Cadena en Mayuscula */
		pos = aux.indexOf('+');

		while (pos != -1)
		{
			if (pos == (aux.length() - 1))
			{
				aux = aux.substring(0, pos);
			}
			else
			{
				posEspace = aux.indexOf(' ', pos + 1);

				if (posEspace != -1)
				{
					aux = aux.substring(0, pos) + aux.substring(pos + 1, posEspace).toUpperCase() + aux.substring(posEspace);
				}
				else
				{
					aux = aux.substring(0, pos) + aux.substring(pos + 1).toUpperCase();
				}
			}

			pos = aux.indexOf('+', pos + 1);
		}

		aux = aux.substring(0, 1).toUpperCase() + aux.substring(1);

		return aux;
	}

}
