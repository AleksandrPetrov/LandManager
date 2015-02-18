package com.land.client.ui.form;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class SpanWidget extends Widget implements HasClickHandlers {

    public SpanWidget() {
        super();
        setElement(DOM.createSpan());
    }

    public SpanWidget(String classAtr, String html) {
        this();
        setStyleName(classAtr);
        setHTML(html);
    }

    public void setHTML(String text) {
        getElement().setInnerHTML(text);
    }

    public String getHTML() {
        return getElement().getInnerHTML();
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

}
