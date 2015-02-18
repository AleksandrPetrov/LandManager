package com.land.shared.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ObjectExpensesItemDto implements IDto {

	private ObjectDto landObject = null;

	private List<ExpenseDto> expences = new ArrayList<ExpenseDto>();

	public ObjectExpensesItemDto() {
		super();
	}

	public ObjectDto getLandObject() {
		return landObject;
	}

	public void setLandObject(ObjectDto landObject) {
		this.landObject = landObject;
	}

	public List<ExpenseDto> getExpences() {
		return expences;
	}

	public void setExpences(List<ExpenseDto> expences) {
		this.expences = expences;
	}

	public ObjectExpensesItemDto clone() {
		ObjectExpensesItemDto res = new ObjectExpensesItemDto();
		res.setLandObject(landObject == null ? null : landObject.clone());
		for (ExpenseDto expense : expences)
			res.getExpences().add(expense.clone());
		return res;
	}
}
