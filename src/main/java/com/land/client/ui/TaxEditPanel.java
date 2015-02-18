package com.land.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.form.LabelWidget;
import com.land.client.ui.form.LegendWidget;
import com.land.client.ui.form.SpanWidget;
import com.land.client.ui.form.StyledFlowPanel;
import com.land.shared.dto.FileDto;
import com.land.shared.dto.TaxDto;

public class TaxEditPanel extends Composite {

    private NumberFormat fmt = NumberFormat.getFormat("0.00");

    private LegendWidget addressLegend = new LegendWidget();

    //@formatter:off
            private LabelWidget yearHelp = new LabelWidget("help-inline", null, "");
            private TextBox yearInput = new TextBox();
        private FlowPanel yearControl = new StyledFlowPanel("controls", yearInput, yearHelp);
        private LabelWidget yearLabel = new LabelWidget("control-label", "yearInput", "Год");
    private FlowPanel yearPanel = new StyledFlowPanel("control-group", yearLabel, yearControl);

            private LabelWidget valueHelp = new LabelWidget("help-inline", null, "");
                private TextBox valueInput = new TextBox();
                private SpanWidget valueInputAdd = new SpanWidget("add-on", "р.");
            private FlowPanel valueInputContainer = new StyledFlowPanel("input-append", valueInput, valueInputAdd);
        private FlowPanel valueControl = new StyledFlowPanel("controls", valueInputContainer, valueHelp);
        private LabelWidget valueLabel = new LabelWidget("control-label", "valueInput", "Сумма");
    private FlowPanel valuePanel = new StyledFlowPanel("control-group", valueLabel, valueControl);

            private FileUploadButton receiptUpload = new FileUploadButton(null) {
                protected void onStartSubmit(int submitId, List<SubmitFile> files) {
                    this.setEnabled(false);
                    WaitPanel.show();
                }
                protected void onSubmitError(String error) {
                    this.setEnabled(true);
                    WaitPanel.hide();
                    Window.alert(error);
                }
                protected void onSubmitComplete(int submitId, List<FileDto> files) {
                    this.setEnabled(true);
                    WaitPanel.hide();
                    if (files.size() > 0) {
                        setReceipt(files.get(0));
                    }
                }
            };
            private FileWDelAnchor receiptAnchor = new FileWDelAnchor();
        private FlowPanel receiptControl = new StyledFlowPanel("controls", receiptAnchor, receiptUpload);
        private LabelWidget receiptLabel = new LabelWidget("control-label", "receiptInput", "Квитанция");
    private FlowPanel receiptPanel = new StyledFlowPanel("control-group", receiptLabel, receiptControl);
    //@formatter:on
    private StyledFlowPanel panel = new StyledFlowPanel("TaxEditPanel form-horizontal", addressLegend, yearPanel, valuePanel, receiptPanel);

    private TaxDto object = null;

    public TaxEditPanel() {
        super();
        initWidget(panel);
        ChangeHandler validateHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                validate();
            }
        };

        yearInput.getElement().setId("yearInput");
        yearInput.getElement().setAttribute("placeholder", "Год");
        yearInput.addChangeHandler(validateHandler);

        valueInput.getElement().setId("valueInput");
        valueInput.getElement().setAttribute("placeholder", "Сумма");
        valueInput.addChangeHandler(validateHandler);

        receiptAnchor.addDeleteClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                setReceipt(null);
            }
        });

        receiptUpload.setMultiple(false);
    }

    private void setAddress(String address) {
        addressLegend.setText(address);
    }

    private void setObject(TaxDto tax) {
        if (tax == null) tax = new TaxDto();
        tax = tax.clone();
        this.object = tax;

        yearInput.setText(tax.getYear() + "");
        valueInput.setValue(fmt.format(tax.getValue()));

        setReceipt(tax.getReceipt());
    }

    private void setReceipt(FileDto receipt) {
        receiptAnchor.setFile(receipt);
        if (receipt == null) {
            receiptUpload.setHtml("<i class='icon-plus'></i>Загрузить файл..");
            receiptAnchor.getElement().getStyle().setDisplay(Display.NONE);
            receiptAnchor.getElement().getStyle().setVisibility(Visibility.HIDDEN);
        }
        else {
            receiptUpload.setHtml("<i class='icon-plus'></i>Заменить файл..");
            receiptAnchor.getElement().getStyle().clearDisplay();
            receiptAnchor.getElement().getStyle().clearVisibility();
        }
    }

    public boolean validate() {
        boolean valid = true;

        try {
            long l = Long.parseLong(yearInput.getText());
            if (l < 0) {
                yearPanel.addStyleName("error");
                yearHelp.setText("Год не должна быть отрицательным");
                valid = false;
            }
            else {
                yearPanel.removeStyleName("error");
                yearHelp.setText("");
            }
        }
        catch (Exception e) {
            yearPanel.addStyleName("error");
            yearHelp.setText("Год должен быть задан в формате ГГГГ");
            valid = false;
        }

        try {
            double d = fmt.parse(valueInput.getText().replace(".", ","));
            if (d < 0) {
                valuePanel.addStyleName("error");
                valueHelp.setText("Сумма не должна быть отрицательной");
                valid = false;
            }
            else {
                valuePanel.removeStyleName("error");
                valueHelp.setText("");
            }
        }
        catch (Exception e) {
            valuePanel.addStyleName("error");
            valueHelp.setText("Сумма должна быть записана в формате X.XX");
            valid = false;
        }

        return valid;
    }

    public TaxDto getObject() {
        Integer year = null;
        try {
            year = Integer.parseInt(yearInput.getText());
        }
        catch (Exception e) {}
        object.setYear(year);

        Double value = null;
        try {
            value = fmt.parse(valueInput.getText().replace(".", ","));
        }
        catch (Exception e) {}
        object.setValue(value);

        object.setReceipt(receiptAnchor.getFile());
        return object;
    }

    public static void showInPopup(TaxDto tax, String address, final IPopupHandler<TaxDto> callback) {
        final TaxEditPanel editPanel = new TaxEditPanel();
        editPanel.setObject(tax);
        editPanel.setAddress(address);

        final ModalPanel mp = new ModalPanel();
        if (tax == null || tax.getId() == null) {
            mp.setHeader("Добавить налог");
            mp.setButtonText(EModalResult.SAVE, "Добавить");
            mp.setButtonVisible(EModalResult.DELETE, false);
        }
        else {
            mp.setHeader("Изменить налог");
            mp.setButtonText(EModalResult.SAVE, "Сохранить");
            mp.setButtonVisible(EModalResult.DELETE, true);
        }
        mp.setContent(editPanel);
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.DELETE) {
                    callback.onDelete(mp, editPanel.getObject());
                    return true;
                }
                else if (result == EModalResult.SAVE) {
                    if (editPanel.validate()) {
                        callback.onSave(mp, editPanel.getObject());
                        return true;
                    }
                    else
                        return false;
                }
                else {
                    callback.onCancel(mp);
                    return true;
                }
            }
        });
        mp.show();
    }

}
