package com.land.server.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.land.shared.dto.TaxDto;

@Entity
@Table(name = "T_TAX", uniqueConstraints = @UniqueConstraint(columnNames = { "c_object", "c_year" }))
public class Tax extends AbstractEntity {

	@Column(name = "c_year", nullable = false)
	private Integer year;

	@Column(name = "c_value", nullable = false, precision = 19, scale = 2)
	private Double value;

	@JoinColumn(name = "c_object", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private LandObject object;

	@JoinColumn(name = "c_receipt", nullable = true)
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private File receipt;

	public Tax() {
		super();
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

	public LandObject getObject() {
		return object;
	}

	public void setObject(LandObject object) {
		this.object = object;
	}

	public File getReceipt() {
		return receipt;
	}

	public void setReceipt(File receipt) {
		this.receipt = receipt;
	}

	public TaxDto toDto() {
		TaxDto dto = new TaxDto();
		dto.setId(id);
		dto.setObjectId(object == null ? null : object.getId());
		dto.setYear(year);
		dto.setValue(value);
		dto.setReceipt(receipt == null ? null : receipt.toDto());
		return dto;
	}
}
