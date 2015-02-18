package com.land.shared.dto;

@SuppressWarnings("serial")
public class TaxDto extends EntityDto {

	private Long objectId;

	private Integer year;

	private Double value;

	private FileDto receipt;

	public TaxDto() {
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

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public FileDto getReceipt() {
		return receipt;
	}

	public void setReceipt(FileDto receipt) {
		this.receipt = receipt;
	}

	public TaxDto clone() {
		TaxDto res = new TaxDto();
		res.setId(id);
		res.setObjectId(objectId);
		res.setYear(year);
		res.setValue(value);
		res.setReceipt(receipt == null ? null : receipt.clone());
		return res;
	}

}
