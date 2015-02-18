package com.land.client.ui.form;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class CheckBoxWidget extends Widget implements HasValue<Boolean>, HasClickHandlers {

    InputElement inputElem = InputElement.as(DOM.createInputCheck());

    public CheckBoxWidget() {
        super();
        setElement(DOM.createLabel());
    }

    public CheckBoxWidget(String style, String text) {
        this();
        getElement().setClassName(style);
        setText(text);
        getElement().appendChild(inputElem);
    }

    public void setText(String text) {
        getElement().setInnerText(text);
    }

    public String getText() {
        return getElement().getInnerText();
    }

    private boolean valueChangeHandlerInitialized;

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        // Is this the first value change handler? If so, time to add handlers
        if (!valueChangeHandlerInitialized) {
            ensureDomEventHandlers();
            valueChangeHandlerInitialized = true;
        }
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    protected void ensureDomEventHandlers() {
        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Checkboxes always toggle their value, no need to compare
                // with old value. Radio buttons are not so lucky, see
                // overrides in RadioButton
                ValueChangeEvent.fire(CheckBoxWidget.this, getValue());
            }
        });
    }

    @Override
    public Boolean getValue() {
        if (isAttached()) {
            return inputElem.isChecked();
        }
        else {
            return inputElem.isDefaultChecked();
        }
    }

    @Override
    public void setValue(Boolean value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        if (value == null) {
            value = Boolean.FALSE;
        }

        Boolean oldValue = getValue();
        inputElem.setChecked(value);
        inputElem.setDefaultChecked(value);
        if (value.equals(oldValue)) {
            return;
        }
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

}
