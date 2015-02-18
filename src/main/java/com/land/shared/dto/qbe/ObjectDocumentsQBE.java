package com.land.shared.dto.qbe;

@SuppressWarnings("serial")
public class ObjectDocumentsQBE extends AbstractQBE {

	private Long objectId = null;

	public ObjectDocumentsQBE() {
	}

	public ObjectDocumentsQBE(Long first, Long count) {
		super(first, count);
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

}
