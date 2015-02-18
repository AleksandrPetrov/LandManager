package com.land.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.land.shared.dto.ObjectDto;

@Entity
@Table(name = "T_OBJECT")
public class LandObject extends AbstractEntity {

	@Column(name = "c_address", length = 1024, nullable = false)
	private String address = "";

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "object", cascade = CascadeType.ALL)
	private List<File> documents = new ArrayList<File>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "object", cascade = CascadeType.ALL)
	private List<Tax> taxes = new ArrayList<Tax>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "object", cascade = CascadeType.ALL)
	private List<Income> incomes = new ArrayList<Income>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "object", cascade = CascadeType.ALL)
	private List<Expense> expenses = new ArrayList<Expense>();

	@Column(name = "c_taxes_view", nullable = false)
	private Boolean taxesView;

	@Column(name = "c_expences_view", nullable = false)
	private Boolean expencesView;

	@Column(name = "c_incomes_view", nullable = false)
	private Boolean incomesView;

	public LandObject() {
		super();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<File> getDocuments() {
		return documents;
	}

	public void setDocuments(List<File> documents) {
		this.documents = documents;
	}

	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public List<Income> getIncomes() {
		return incomes;
	}

	public void setIncomes(List<Income> incomes) {
		this.incomes = incomes;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	public Boolean getTaxesView() {
		return taxesView;
	}

	public void setTaxesView(Boolean taxesView) {
		this.taxesView = taxesView;
	}

	public Boolean getExpencesView() {
		return expencesView;
	}

	public void setExpencesView(Boolean expencesView) {
		this.expencesView = expencesView;
	}

	public Boolean getIncomesView() {
		return incomesView;
	}

	public void setIncomesView(Boolean incomesView) {
		this.incomesView = incomesView;
	}

	public ObjectDto toDto() {
		ObjectDto dto = new ObjectDto();
		dto.setId(id);
		dto.setAddress(address);
		dto.setTaxesView(taxesView);
		dto.setExpencesView(expencesView);
		dto.setIncomesView(incomesView);
		return dto;
	}
}
