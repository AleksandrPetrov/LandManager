package com.land.shared.dto;

import java.util.Date;

@SuppressWarnings("serial")
public class ExpenseDto extends EntityDto {

	private Long objectId;

	private String service;

	private Date date;

	private Double sumToPay;

	private Double sumPayed;

	private FileDto receipt;

	public ExpenseDto() {
		super();
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
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

	public Double countDebt() {
		return sumToPay - sumPayed;
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

	public FileDto getReceipt() {
		return receipt;
	}

	public void setReceipt(FileDto receipt) {
		this.receipt = receipt;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public ExpenseDto clone() {
		ExpenseDto res = new ExpenseDto();
		res.setId(id);
		res.setService(service);
		res.setObjectId(objectId);
		res.setDate(date);
		res.setSumToPay(sumToPay);
		res.setSumPayed(sumPayed);
		res.setReceipt(receipt == null ? null : receipt.clone());
		return res;
	}

}
