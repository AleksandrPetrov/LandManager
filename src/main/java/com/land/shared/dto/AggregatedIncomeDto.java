package com.land.shared.dto;

@SuppressWarnings("serial")
public class AggregatedIncomeDto implements IDto {

	private Long objectId;
	private Long year;
	private Long month;
	private Double sum;

	public AggregatedIncomeDto() {
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

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public AggregatedIncomeDto clone() {
		AggregatedIncomeDto res = new AggregatedIncomeDto();
		res.setObjectId(objectId);
		res.setYear(year);
		res.setMonth(month);
		res.setSum(sum);
		return res;
	}

}
