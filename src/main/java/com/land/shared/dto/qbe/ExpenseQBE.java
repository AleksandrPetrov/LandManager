package com.land.shared.dto.qbe;

import java.util.List;

@SuppressWarnings("serial")
public class ExpenseQBE extends AbstractQBE {

	private List<Long> objectIdList = null;

	private Long year = null;

	public ExpenseQBE() {
		super();
	}

	public ExpenseQBE(Long first, Long count) {
		super(first, count);
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public List<Long> getObjectIdList() {
		return objectIdList;
	}

	public void setObjectIdList(List<Long> objectIdList) {
		this.objectIdList = objectIdList;
	}

}
