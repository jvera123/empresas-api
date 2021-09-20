package com.banelco.empresas.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "Prepago")
public class Prepago {
	@Id
	@Column(name = "codigo", length = 4, unique = true, nullable = false)
	private String codigo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumns({
			@JoinColumn(name = "id_rubro", referencedColumnName = "id_rubro", nullable = false, insertable = true, updatable = true),
			@JoinColumn(name = "tipo", referencedColumnName = "tipo", nullable = false, insertable = true, updatable = true),
			@JoinColumn(name = "fiid_rubro", referencedColumnName = "fiid", nullable = false, insertable = true, updatable = true) })
	private Rubro rubro;

	@Column(name = "descripcion", nullable = true)
	private String descripcion;

	@Column(name = "datos_ticket", nullable = true)
	private int datosTicket;

	@Column(name = "nro_leyenda", nullable = true)
	private int nroLeyenda;

	@Column(name = "orden", nullable = true)
	private int orden;

	@Column(name = "montos", nullable = true)
	private String montos;

	@Column(name = "id_refresh", nullable = false)
	private String idRefresh;

	@Column(name = "habilitado", nullable = false)
	private boolean habilitado = true;
}
