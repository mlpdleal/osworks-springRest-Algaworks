package com.algaworks.osworks.api.model;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OrdemServicoInputModel {
	
	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal preco;
	
	@Valid
	@NotNull
	private ClienteIdInputModel cliente;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	public ClienteIdInputModel getCliente() {
		return cliente;
	}
	public void setCliente(ClienteIdInputModel cliente) {
		this.cliente = cliente;
	}

}
