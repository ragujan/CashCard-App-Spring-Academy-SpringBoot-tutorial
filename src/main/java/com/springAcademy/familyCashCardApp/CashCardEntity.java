package com.springAcademy.familyCashCardApp;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CashCardEntity(Long id, Double amount) {
		super();
		this.id = id;
		this.amount = amount;
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
