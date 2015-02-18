package com.land.server.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.land.shared.dto.FileDto;

@Entity
@Table(name = "T_FILE")
public class File extends AbstractEntity {

	@Column(name = "c_name", length = 1024, nullable = false)
	private String name;

	@Column(name = "c_size", nullable = false)
	private Long size;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "c_create_date", nullable = false)
	private Date createDate;

	@Column(name = "c_owner", nullable = false)
	private String owner;

	@JoinColumn(name = "c_blob", nullable = false)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private BlobEntity blob;

	@Column(name = "c_content_type", length = 255, nullable = false)
	private String contentType;

	@JoinColumn(name = "c_object", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private LandObject object;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "receipt", cascade = CascadeType.PERSIST)
	private Tax tax;

	@PrePersist
	protected void onCreate() {
		createDate = new Date();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BlobEntity getBlob() {
		return blob;
	}

	public void setBlob(BlobEntity content) {
		this.blob = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public LandObject getObject() {
		return object;
	}

	public void setObject(LandObject object) {
		this.object = object;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public FileDto toDto() {
		FileDto dto = new FileDto();
		dto.setId(id);
		dto.setName(name);
		dto.setSize(size);
		dto.setCreateDate(createDate);
		dto.setOwner(owner);
		return dto;
	}
}
