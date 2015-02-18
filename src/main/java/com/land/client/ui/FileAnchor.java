package com.land.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.land.shared.dto.FileDto;

public class FileAnchor extends Composite {

	private Anchor anchor = new Anchor();

	private FileDto file;

	public FileAnchor() {
		super();
		initWidget(anchor);
		anchor.setTarget("_blank");
		addStyleName("file");
	}

	public FileAnchor(FileDto file) {
		this();
		setFile(file);
	}

	public void setFile(FileDto file) {
		this.file = file;
		Long id = file == null ? null : file.getId();
		String name = file == null ? "" : file.getName();
		anchor.setHref(getGetFileServletUrl(name, id));
		setText(name);
		anchor.setTitle(name);
	}

	public FileDto getFile() {
		return file;
	}

	public void setText(String text) {
		anchor.setHTML("<i class='icon-file'></i>" + text);
	}

	private static String getGetFileServletUrl(String name, Long id) {
		return getServletUrl("service/getfile/", name, id);
	}

	private static String getServletUrl(String servlet, String name, Long id) {
		UrlBuilder builder = new UrlBuilder();
		builder.setParameter("id", (id + ""));
		builder.setProtocol("http");
		builder.setPath(GWT.getHostPageBaseURL() + servlet + name.replace("х", "x"));

		return builder.buildString().substring(8);
	}
}
