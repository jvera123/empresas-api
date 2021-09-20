package com.banelco.empresas.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Empresa")
public class Empresa {
	@EmbeddedId
	private EmpresaID idEmbeddedIdEmpresa;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumns({
			@JoinColumn(name = "id_rubro", referencedColumnName = "id_rubro", nullable = false, insertable = true, updatable = true),
			@JoinColumn(name = "tipo", referencedColumnName = "tipo", nullable = false, insertable = true, updatable = true),
			@JoinColumn(name = "fiid_rubro", referencedColumnName = "fiid", nullable = false, insertable = true, updatable = true) })
	private Rubro rubro;

	@Column(name = "orden")
	private int orden = 0;

	@Column(name = "tipo_empresa", nullable = true)
	private String tipoEmpresa;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "permite_usd")
	private boolean permiteUsd = true;

	@Column(name = "leyenda_subsidios", nullable = true)
	private String leyendaSubsidios = null;

	@Column(name = "leyenda_subsidios_pa", nullable = true)
	private String leyendaSubsidiosPA = null;

	@Column(name = "titulo_identificacion")
	private String tituloIdentificacion;

	@Column(name = "tipo_adhesion")
	private Integer tipoAdhesion;

	@Column(name = "tipo_pago", nullable = true)
	private Integer tipoPago;

	@Column(name = "solo_consultas")
	private boolean soloConsultas = false;

	@Column(name = "importe_permitido")
	private Integer importePermitido;

	@Column(name = "codigo_moneda")
	private Integer codigoMoneda;

	@Column(name = "dato_adicional")
	private String datoAdicional = "";

	@Column(name = "permite_pagos_recurrentes")
	private boolean permitePagosRecurrentes = false;

	@Column(name = "permite_doble_facturacion")
	private boolean permiteDobleFacturacion = false;

	@Column(name = "permite_pagos_tcv")
	private boolean permitePagosTCV = false;

	@Column(name = "permite_pagos_tcm")
	private boolean permitePagosTCM = false;

	@Column(name = "permite_pagos_tca")
	private boolean permitePagosTCA = false;

	@Column(name = "permite_pagos_tdv")
	private boolean permitePagosTDV = false;

	@Column(name = "permite_pagos_tdm")
	private boolean permitePagosTDM = false;

	@Column(name = "monto_minimo_tc")
	private double montoMinimoTC;

	@Column(name = "monto_maximo_tc")
	private double montoMaximoTC;

	@Column(name = "permite_ebpp")
	private boolean permiteEBPP = false;

	@Column(name = "cantidad_vencimientos")
	private Integer cantidadVencimientos;

	@Column(name = "longitud_clave_minima")
	private Integer longitudClaveMinima;

	@Column(name = "longitud_clave_maxima")
	private Integer longitudClaveMaxima;

	@Column(name = "cuit")
	private String cuit;

	@Column(name = "id_refresh", nullable = false)
	private String idRefresh;

	@Column(name = "habilitado", nullable = false)
	private boolean habilitado = true;
}
