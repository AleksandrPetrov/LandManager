package com.land.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.land.client.ui.form.StyledFlowPanel;

public class ModalPanel extends Composite {

    public enum EModalResult {
        SAVE,
        DELETE,
        CANCEL
    }

    public interface IModalListener {
        public boolean onClose(EModalResult result);

    }

    private HandlerRegistration nativeRegistration = null;
    private FlowPanel glass = new StyledFlowPanel("modal-backdrop fade");
    private FlowPanel panel = new StyledFlowPanel("modal fade");

    private FlowPanel headerPanel = new StyledFlowPanel("modal-header");
    private Element headerLabel = Document.get().createHElement(3).cast();
    private Button closeButton = new Button("×");

    private SimplePanel bodyPanel = new SimplePanel();
    private FlowPanel footerPanel = new StyledFlowPanel("modal-footer");
    private Button deleteButton = new Button("Удалить");
    private Button saveButton = new Button("Сохранить");
    private Button cancelButton = new Button("Отменить");

    private IModalListener modalListener = null;
    private HandlerRegistration hr = null;
    private boolean autoHide = false;

    public ModalPanel() {
        this(true);
    }

    public ModalPanel(boolean autoHide) {
        super();
        initWidget(panel);
        this.autoHide = autoHide;

        panel.add(headerPanel);
        closeButton.setStyleName("close");
        headerPanel.add(closeButton);
        headerLabel.setInnerText("Заголовок");
        headerPanel.getElement().appendChild(headerLabel);

        bodyPanel.setStyleName("modal-body");
        panel.add(bodyPanel);

        panel.add(footerPanel);
        deleteButton.setStyleName("btn btn-danger");
        footerPanel.add(deleteButton);
        saveButton.setStyleName("btn btn-primary");
        footerPanel.add(saveButton);
        cancelButton.setStyleName("btn");
        footerPanel.add(cancelButton);

        deleteButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                buttonClickImpl(EModalResult.DELETE);
            }
        });
        saveButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                buttonClickImpl(EModalResult.SAVE);
            }
        });
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                buttonClickImpl(EModalResult.CANCEL);
            }
        });
        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                buttonClickImpl(EModalResult.CANCEL);
            }
        });

    }

    public void setHeader(String headerHtml) {
        headerLabel.setInnerHTML(headerHtml);
    }

    public void setButtonVisible(EModalResult button, boolean visible) {
        switch (button) {
            case DELETE:
                deleteButton.setVisible(visible);
                break;
            case SAVE:
                saveButton.setVisible(visible);
                break;
            case CANCEL:
                cancelButton.setVisible(visible);
                break;
        }
    }

    public void setButtonText(EModalResult button, String html) {
        saveButton.setHTML(html);
    }

    public void setContent(Widget widget) {
        bodyPanel.setWidget(widget);
    }

    public Widget getContent() {
        return bodyPanel.getWidget();
    }

    public void setModalListener(IModalListener modalListener) {
        this.modalListener = modalListener;
    }

    protected void onAttach() {
        super.onAttach();
        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> event) {
                buttonClickImpl(EModalResult.CANCEL);
            }
        });
        new Timer() {
            @Override
            public void run() {
                panel.addStyleName("in");
                glass.addStyleName("in");
            }
        }.schedule(100);

        if (nativeRegistration != null) nativeRegistration.removeHandler();
        nativeRegistration = Event.addNativePreviewHandler(new NativePreviewHandler() {
            public void onPreviewNativeEvent(NativePreviewEvent event) {
                if (event.isCanceled() || event.isConsumed()) {
                    event.cancel();
                    return;
                }
                if (event.isCanceled()) return;

                // If the event targets the popup or the partner, consume it
                Event nativeEvent = Event.as(event.getNativeEvent());
                boolean eventTargetsPopupOrPartner = eventTargetsPopup(nativeEvent);
                if (eventTargetsPopupOrPartner) {
                    event.consume();
                }

                // Cancel the event if it doesn't target the modal popup. Note
                // that the
                // event can be both canceled and consumed.
                event.cancel();
            }
        });
    }

    private boolean eventTargetsPopup(NativeEvent event) {
        EventTarget target = event.getEventTarget();
        if (Element.is(target)) {
            return getElement().isOrHasChild(Element.as(target));
        }
        return false;
    }

    @Override
    protected void onDetach() {
        if (hr != null) hr.removeHandler();
        if (nativeRegistration != null) nativeRegistration.removeHandler();
        super.onDetach();
    }

    public void show() {
        RootPanel.get().add(glass);
        RootPanel.get().add(this);
    }

    public void hide() {
        panel.removeStyleName("in");
        glass.removeStyleName("in");
        new Timer() {
            @Override
            public void run() {
                glass.removeFromParent();
                ModalPanel.this.removeFromParent();
            }
        }.schedule(300);
    }

    private void buttonClickImpl(EModalResult modalResult) {
        if (modalListener == null || modalListener.onClose(modalResult)) {
            if (autoHide) hide();
        }
    }

}
