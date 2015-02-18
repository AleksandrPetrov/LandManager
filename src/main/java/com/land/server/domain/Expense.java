package com.land.server.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.land.shared.dto.ExpenseDto;

@Entity
@Table(name = "T_EXPENSE")
public class Expense extends AbstractEntity {

	@Column(name = "c_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@JoinColumn(name = "c_service", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private ExpenseService service;

	@Column(name = "c_sum_to_pay", nullable = false, precision = 19, scale = 2)
	private Double sumToPay;

	@Column(name = "c_sum_payed", nullable = false, precision = 19, scale = 2)
	private Double sumPayed;

	@JoinColumn(name = "c_object", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private LandObject object;

	@JoinColumn(name = "c_receipt", nullable = true)
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private File receipt;

	public Expense() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getSumToPay() {
		return sumToPay;
	}

	public void setSumToPay(Double sumToPay) {
		this.sumToPay = sumToPay;
	}

	public Double getSumPayed() {
		return sumPayed;
	}

	public void setSumPayed(Double sumPayed) {
		this.sumPayed = sumPayed;
	}

	public LandObject getObject() {
		return object;
	}

	public void setObject(LandObject object) {
		this.object = object;
	}

	public File getReceipt() {
		return receipt;
	}

	public void setReceipt(File receipt) {
		this.receipt = receipt;
	}

	public ExpenseService getService() {
		return service;
	}

	public void setService(ExpenseService service) {
		this.service = service;
	}

	public ExpenseDto toDto() {
		ExpenseDto res = new ExpenseDto();
		res.setId(id);
		res.setService(service == null ? null : service.getName());
		res.setObjectId(object == null ? null : object.getId());
		res.setDate(date);
		res.setSumToPay(sumToPay);
		res.setSumPayed(sumPayed);
		res.setReceipt(receipt == null ? null : receipt.toDto());
		return res;
	}
}
