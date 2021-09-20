package com.banelco.empresas.model.list;

import java.util.List;

import com.banelco.empresas.model.dto.EmpresaDTO;
import com.banelco.empresas.model.dto.PrepagoDTO;
import com.banelco.empresas.model.entity.Empresa;
import com.banelco.empresas.model.entity.Prepago;

import lombok.Data;

@Data
public class ListasObject
{
	private List<Empresa> listaCorrecta;
	private List<EmpresaDTO> listaErronea;
	private List<Prepago> listaCorrectaPrepago;
	private List<PrepagoDTO> listaErroneaPrepago;

	public ListasObject(List<Empresa> listaCorrecta, List<EmpresaDTO> listaErronea,
			List<Prepago> listaCorrectaPrepago, List<PrepagoDTO> listaErroneaPrepago)
	{
		this.listaCorrecta = listaCorrecta;
		this.listaErronea = listaErronea;
		this.listaCorrectaPrepago = listaCorrectaPrepago;
		this.listaErroneaPrepago = listaErroneaPrepago;
	}
}
