package com.land.client.ui;

import com.google.gwt.user.client.ui.HTML;

public class ConfirmModalPanel extends ModalPanel {

    public ConfirmModalPanel(String title, String text, String deleteButtonText) {
        super();
        setHeader(title);
        setButtonText(EModalResult.DELETE, deleteButtonText);
        setButtonVisible(EModalResult.SAVE, false);
        setContent(new HTML(text));
    }

}
