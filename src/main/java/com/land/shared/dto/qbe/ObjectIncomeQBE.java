package com.land.shared.dto.qbe;

import java.util.List;

@SuppressWarnings("serial")
public class ObjectIncomeQBE extends AbstractQBE {

	private List<Long> objectIdList = null;

	private Long year = null;

	public ObjectIncomeQBE() {
	}

	public ObjectIncomeQBE(Long first, Long count) {
		super(first, count);
	}

	public List<Long> getObjectIdList() {
		return objectIdList;
	}

	public void setObjectIdList(List<Long> objectIdList) {
		this.objectIdList = objectIdList;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

}
