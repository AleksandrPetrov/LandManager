package com.land.shared.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ObjectTaxesItemDto implements IDto {

	private ObjectDto landObject = null;

	private List<TaxDto> taxes = new ArrayList<TaxDto>();

	public ObjectTaxesItemDto() {
		super();
	}

	public ObjectDto getLandObject() {
		return landObject;
	}

	public void setLandObject(ObjectDto landObject) {
		this.landObject = landObject;
	}

	public List<TaxDto> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<TaxDto> taxes) {
		this.taxes = taxes;
	}

	public ObjectTaxesItemDto clone() {
		ObjectTaxesItemDto res = new ObjectTaxesItemDto();
		res.setLandObject(landObject == null ? null : landObject.clone());
		for (TaxDto tax : taxes)
			res.getTaxes().add(tax.clone());
		return res;
	}
}
