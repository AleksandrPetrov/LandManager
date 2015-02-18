package com.land.shared.dto;

@SuppressWarnings("serial")
public class ObjectYearsBoundariesDto extends EntityDto {

	private Long objectCount;
	private Long minYear;
	private Long maxYear;

	public ObjectYearsBoundariesDto() {
		super();
	}

	public Long getObjectCount() {
		return objectCount;
	}

	public void setObjectCount(Long objectCount) {
		this.objectCount = objectCount;
	}

	public Long getMinYear() {
		return minYear;
	}

	public void setMinYear(Long minYear) {
		this.minYear = minYear;
	}

	public Long getMaxYear() {
		return maxYear;
	}

	public void setMaxYear(Long maxYear) {
		this.maxYear = maxYear;
	}

	public ObjectYearsBoundariesDto clone() {
		ObjectYearsBoundariesDto res = new ObjectYearsBoundariesDto();
		res.setId(id);
		res.setObjectCount(objectCount);
		res.setMinYear(minYear);
		res.setMaxYear(maxYear);
		return res;
	}

}
