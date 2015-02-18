package com.land.shared.dto.qbe;

@SuppressWarnings("serial")
public class ObjectQBE extends AbstractQBE {

	private Boolean taxesView = null;

	private Boolean expencesView = null;

	private Boolean incomesView = null;

	public ObjectQBE() {
	}

	public ObjectQBE(int first, int count) {
		super(first, count);
	}

	public ObjectQBE(Long first, Long count) {
		super(first, count);
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

}
