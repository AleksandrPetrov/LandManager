package com.land.shared.dto;

@SuppressWarnings("serial")
public class IncomeDto extends EntityDto {

	private Long objectId;

	private Long year;

	private Long month;

	private Double value;

	public IncomeDto() {
		super();
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
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

	public IncomeDto clone() {
		IncomeDto res = new IncomeDto();
		res.setId(id);
		res.setObjectId(objectId);
		res.setYear(year);
		res.setMonth(month);
		res.setValue(value);
		return res;
	}

}
