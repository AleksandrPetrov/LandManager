package com.land.shared.dto;

@SuppressWarnings("serial")
public class ObjectDto extends EntityDto {

	private String address = "";

	private Boolean taxesView = true;

	private Boolean expencesView = true;

	private Boolean incomesView = true;

	public ObjectDto() {
		super();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getTaxesView() {
		return taxesView;
	}

	public void setTaxesView(Boolean taxesView) {
		this.taxesView = taxesView;
	}

	public Boolean getExpencesView() {
		return expencesView;
	}

	public void setExpencesView(Boolean expencesView) {
		this.expencesView = expencesView;
	}

	public Boolean getIncomesView() {
		return incomesView;
	}

	public void setIncomesView(Boolean incomesView) {
		this.incomesView = incomesView;
	}

	public ObjectDto clone() {
		ObjectDto res = new ObjectDto();
		res.setId(id);
		res.setAddress(address);
		res.setTaxesView(taxesView);
		res.setExpencesView(expencesView);
		res.setIncomesView(incomesView);
		return res;
	}

}
