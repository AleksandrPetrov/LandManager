package com.land.client.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.land.client.ui.form.StyledFlowPanel;

public class WaitPanel extends Composite {

    private static final Timer fadeInTimer = new Timer() {
        @Override
        public void run() {
            if (glass.isAttached()) glass.addStyleName("in");
        }
    };

    private static final Timer fadeTimer = new Timer() {
        @Override
        public void run() {
            glass.removeFromParent();
            glass.removeStyleName("in");
        }
    };

    private static final FlowPanel glass = new StyledFlowPanel("modal-loading modal-backdrop fade") {
        protected void onAttach() {
            super.onAttach();
            fadeInTimer.schedule(100);
        }
    };

    public static void show() {
        fadeTimer.cancel();
        RootPanel.get().add(glass);
    }

    public static void hide() {
        fadeInTimer.cancel();
        fadeTimer.cancel();
        glass.removeStyleName("in");
        fadeTimer.schedule(300);
    }

}
