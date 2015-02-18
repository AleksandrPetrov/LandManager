package com.land.shared.dto;

import java.util.Date;

/** Файл */
@SuppressWarnings("serial")
public class FileDto extends EntityDto {

	private String name;

	private Long size;

	private Date createDate;

	private String owner;

	public FileDto() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public FileDto clone() {
		FileDto res = new FileDto();
		res.setId(id);
		res.setName(name);
		res.setSize(size);
		res.setCreateDate(createDate);
		res.setOwner(owner);
		return res;
	}
}
