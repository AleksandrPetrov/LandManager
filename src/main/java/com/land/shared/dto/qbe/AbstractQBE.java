package com.land.shared.dto.qbe;

import com.land.shared.dto.IDto;

@SuppressWarnings("serial")
public class AbstractQBE implements IDto {

	private int first = 0;
	private int count = -1;

	public AbstractQBE() {
		super();
	}

	public AbstractQBE(int first, int count) {
		super();
		this.first = first;
		this.count = count;
	}

	public AbstractQBE(Long first, Long count) {
		this(first.intValue(), count.intValue());
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
