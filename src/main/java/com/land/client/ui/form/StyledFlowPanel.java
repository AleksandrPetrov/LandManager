package com.land.client.ui.form;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class StyledFlowPanel extends FlowPanel {

	public StyledFlowPanel() {
		super();
	}

	public StyledFlowPanel(String style, Widget... widgets) {
		super();
		addStyleName(style);
		for (Widget widget : widgets) {
			add(widget);
		}
	}

}
