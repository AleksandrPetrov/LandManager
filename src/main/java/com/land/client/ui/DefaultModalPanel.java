package com.land.client.ui;

public class DefaultModalPanel extends ModalPanel {

    public DefaultModalPanel(boolean create, String objectType) {
        super();
        setButtonVisible(EModalResult.DELETE, !create);
        if (create) {
            setHeader("Добавить " + objectType);
            setButtonText(EModalResult.SAVE, "Добавить");
        }
        else {
            setHeader("Изменить " + objectType);
            setButtonText(EModalResult.SAVE, "Изменить");
        }
    }

}
