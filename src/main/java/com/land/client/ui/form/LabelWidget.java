package com.land.client.ui.form;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class LabelWidget extends Widget {

    public LabelWidget() {
        super();
        setElement(DOM.createLabel());
    }

    public LabelWidget(String classAtr, String forAtr, String text) {
        this();
        setStyleName(classAtr);
        if (forAtr != null) getElement().setAttribute("for", forAtr);
        setText(text);
    }

    public LabelWidget(String classAtr, Widget widget) {
        this(classAtr, null, null);
    }

    public void setText(String text) {
        if (text == null) text = "";
        getElement().setInnerText(text);
    }

    public String getText() {
        return getElement().getInnerText();
    }

}
