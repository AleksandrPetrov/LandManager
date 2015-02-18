package com.land.shared.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ObjectIncomesItemDto implements IDto {

	private ObjectDto landObject = null;

	private List<IncomeDto> incomes = new ArrayList<IncomeDto>();

	public ObjectIncomesItemDto() {
		super();
	}

	public ObjectDto getLandObject() {
		return landObject;
	}

	public void setLandObject(ObjectDto landObject) {
		this.landObject = landObject;
	}

	public List<IncomeDto> getIncomes() {
		return incomes;
	}

	public void setIncomes(List<IncomeDto> incomes) {
		this.incomes = incomes;
	}

	public ObjectIncomesItemDto clone() {
		ObjectIncomesItemDto res = new ObjectIncomesItemDto();
		res.setLandObject(landObject == null ? null : landObject.clone());
		for (IncomeDto agi : incomes)
			res.getIncomes().add(agi.clone());
		return res;
	}
}
