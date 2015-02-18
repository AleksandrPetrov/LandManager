package com.land.shared.dto;

@SuppressWarnings("serial")
public class AggregatedTaxDto implements IDto {

	private Long objectId;
	private Integer year;
	private Double sum;

	public AggregatedTaxDto() {
		super();
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public AggregatedTaxDto clone() {
		AggregatedTaxDto res = new AggregatedTaxDto();
		res.setObjectId(objectId);
		res.setYear(year);
		res.setSum(sum);
		return res;
	}

}
