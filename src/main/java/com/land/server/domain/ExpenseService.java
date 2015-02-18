package com.land.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "T_EXPENSE_SERVICE")
public class ExpenseService extends AbstractEntity {

	@Column(name = "c_name", length = 1024, nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL)
	private List<Expense> expenses = new ArrayList<Expense>();

	public ExpenseService() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

}
