package com.land.shared.dto.qbe;

@SuppressWarnings("serial")
public class FileQBE extends AbstractQBE {

	private Long objectId;

	public FileQBE() {
	}

	public FileQBE(Long first, Long count) {
		super(first, count);
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

}
