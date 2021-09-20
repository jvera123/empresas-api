package com.banelco.empresas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Institucion")
public class Institucion {
	@Id
	@Column(name = "consumer_id")
	private String consumerId;

	@Column(name = "refresh_fiid")
	private String refreshFiid;
}
