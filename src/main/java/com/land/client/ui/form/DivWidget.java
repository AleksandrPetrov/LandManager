package com.land.client.ui.form;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class DivWidget extends Widget {

	public DivWidget() {
		super();
		setElement(DOM.createDiv());
	}

	public DivWidget(String text) {
		this();
		setText(text);
	}

	public DivWidget(String text, String style) {
		this(text);
		setStyleName(style);
	}

	public void setText(String text) {
		getElement().setInnerHTML(text);
	}

	public String getText() {
		return getElement().getInnerHTML();
	}

}
