package com.land.shared.dto;

@SuppressWarnings("serial")

public class AggregatedExpensesDto implements IDto {
	private Long objectId;
	private Long year;
	private Long month;
	private Double sumToPay;
	private Double sumPayed;

	public AggregatedExpensesDto() {
		super();
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Long getMonth() {
		return month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}

	public Double getSumPayed() {
		return sumPayed;
	}

	public void setSumPayed(Double sumPayed) {
		this.sumPayed = sumPayed;
	}

	public Double getSumToPay() {
		return sumToPay;
	}

	public void setSumToPay(Double sumToPay) {
		this.sumToPay = sumToPay;
	}

	public Double countDebt() {
		return sumToPay - sumPayed;
	}

	public AggregatedExpensesDto clone() {
		AggregatedExpensesDto res = new AggregatedExpensesDto();
		res.setYear(year);
		res.setMonth(month);
		res.setSumPayed(sumPayed);
		res.setSumToPay(sumToPay);
		return res;
	}
}
