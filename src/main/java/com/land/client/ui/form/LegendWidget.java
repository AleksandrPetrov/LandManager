package com.land.client.ui.form;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class LegendWidget extends Widget {
	public LegendWidget() {
		super();
		setElement(DOM.createLegend());
	}

	public LegendWidget(String text) {
		this();
		setText(text);
	}

	public void setText(String text) {
		getElement().setInnerText(text);
	}

	public String getText() {
		return getElement().getInnerText();
	}
}