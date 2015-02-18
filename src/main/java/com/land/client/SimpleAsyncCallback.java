package com.land.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.land.client.ui.WaitPanel;

public abstract class SimpleAsyncCallback<T> implements AsyncCallback<T> {
    public void onFailure(Throwable caught) {
        WaitPanel.hide();
        Window.alert(caught.getMessage());
    }
}
