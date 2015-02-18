package com.land.server.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.land.shared.dto.IncomeDto;

@Entity
@Table(name = "T_INCOME", uniqueConstraints = @UniqueConstraint(columnNames = { "c_object", "c_year", "c_month" }))
public class Income extends AbstractEntity {

	@Column(name = "c_year", nullable = false)
	private Long year;

	@Column(name = "c_month", nullable = false)
	private Long month;

	@Column(name = "c_value", nullable = false, precision = 19, scale = 2)
	private Double value;

	@JoinColumn(name = "c_object", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private LandObject object;

	public Income() {
		super();
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Long getMonth() {
		return month;
	}

	public void setMonth(Long month) {
		this.month = month;
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

	public IncomeDto toDto() {
		IncomeDto res = new IncomeDto();
		res.setId(id);
		res.setObjectId(object == null ? null : object.getId());
		res.setYear(year);
		res.setMonth(month);
		res.setValue(value);
		return res;
	}
}
