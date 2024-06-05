package com.springAcademy.familyCashCardApp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="cash_cards")
public class CashCardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double amount;
	@Column(name = "owner")
	private String owner;
	
	public CashCardEntity(Long id, Double amount) {
		super();
		this.id = id;
		this.amount = amount;
	}

	public CashCardEntity( Double amount, String owner) {
		this.amount = amount;
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public CashCardEntity( Double amount) {
		super();
		this.amount = amount;
	}
	public CashCardEntity() {
		super();
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
