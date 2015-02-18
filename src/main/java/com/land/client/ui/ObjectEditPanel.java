package com.land.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.form.CheckBoxWidget;
import com.land.client.ui.form.LabelWidget;
import com.land.client.ui.form.StyledFlowPanel;
import com.land.shared.dto.ObjectDto;

public class ObjectEditPanel extends Composite {

    //@formatter:off
            private LabelWidget addressHelp = new LabelWidget("help-inline", null, "");
            private TextBox addressInput = new TextBox();
        private FlowPanel addressControl = new StyledFlowPanel("controls", addressInput, addressHelp);
        private LabelWidget addressLabel = new LabelWidget("control-label", "addressInput", "Адресс");
    private FlowPanel addressPanel = new StyledFlowPanel("control-group", addressLabel, addressControl);
    
		    private CheckBoxWidget taxesViewInput = new CheckBoxWidget("checkbox", "Показывать на странице налогов");
		private FlowPanel taxesViewControl = new StyledFlowPanel("controls", taxesViewInput);
	private FlowPanel taxesViewPanel = new StyledFlowPanel("control-group", taxesViewControl);
	
		    private CheckBoxWidget expencesViewInput = new CheckBoxWidget("checkbox", "Показывать на странице расходов");
		private FlowPanel expencesViewControl = new StyledFlowPanel("controls", expencesViewInput);
	private FlowPanel expencesViewPanel = new StyledFlowPanel("control-group", expencesViewControl);

		    private CheckBoxWidget incomesViewInput = new CheckBoxWidget("checkbox", "Показывать на странице доходов");
		private FlowPanel incomesViewControl = new StyledFlowPanel("controls", incomesViewInput);
	private FlowPanel incomesViewPanel = new StyledFlowPanel("control-group", incomesViewControl);
	//@formatter:on

    private StyledFlowPanel panel = new StyledFlowPanel("ObjectEditPanel form-horizontal", addressPanel, taxesViewPanel, expencesViewPanel, incomesViewPanel);

    private ObjectDto object = null;

    public ObjectEditPanel() {
        super();
        initWidget(panel);
        ChangeHandler validateHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                validate();
            }
        };

        addressInput.getElement().setAttribute("placeholder", "Адресс");
        addressInput.getElement().setId("addressInput");
        addressInput.addChangeHandler(validateHandler);
    }

    public boolean validate() {
        boolean valid = true;

        String address = addressInput.getValue();
        if (address.isEmpty()) {
            addressPanel.addStyleName("error");
            addressHelp.setText("Адресс не может быть пустым");
            valid = false;
        }
        else {
            addressPanel.removeStyleName("error");
            addressHelp.setText("");
        }

        return valid;
    }

    private void setObject(ObjectDto object) {
        if (object == null) object = new ObjectDto();
        this.object = object;

        addressInput.setValue(object.getAddress());
        taxesViewInput.setValue(object.getTaxesView());
        expencesViewInput.setValue(object.getExpencesView());
        incomesViewInput.setValue(object.getIncomesView());
    }

    public ObjectDto getObject() {
        object.setAddress(addressInput.getValue());
        object.setTaxesView(taxesViewInput.getValue());
        object.setExpencesView(expencesViewInput.getValue());
        object.setIncomesView(incomesViewInput.getValue());
        return object;
    }

    public static void showInPopup(ObjectDto object, final IPopupHandler<ObjectDto> callback) {
        final ObjectEditPanel editPanel = new ObjectEditPanel();
        editPanel.setObject(object);

        final DefaultModalPanel mp = new DefaultModalPanel(object == null || object.getId() == null, "объект");
        mp.setContent(editPanel);
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.DELETE) callback.onDelete(mp, editPanel.getObject());
                if (result == EModalResult.SAVE) {
                    if (editPanel.validate()) {
                        callback.onSave(mp, editPanel.getObject());
                        return true;
                    }
                    else
                        return false;
                }
                else
                    callback.onCancel(mp);
                return true;
            }
        });
        mp.show();
    }
}
