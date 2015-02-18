package com.land.client.ui;

public interface IPopupHandler<T> {

    void onSave(ModalPanel mp, T item);

    void onDelete(ModalPanel mp, T item);

    void onCancel(ModalPanel mp);

}
