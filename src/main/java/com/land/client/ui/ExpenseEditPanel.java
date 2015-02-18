package com.land.client.ui;

import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.form.LabelWidget;
import com.land.client.ui.form.LegendWidget;
import com.land.client.ui.form.SpanWidget;
import com.land.client.ui.form.StyledFlowPanel;
import com.land.shared.dto.ExpenseDto;
import com.land.shared.dto.FileDto;

@SuppressWarnings("deprecation")
public class ExpenseEditPanel extends Composite {

    private NumberFormat fmt = NumberFormat.getFormat("0.00");

    private LegendWidget addressLegend = new LegendWidget();

    //@formatter:off
            private LabelWidget dateHelp = new LabelWidget("help-inline", null, "");
                private DateBox dateInput = new DateBox();
                private SpanWidget dateInputAdd = new SpanWidget("add-on", "<i class='icon-calendar'></i>");
            private FlowPanel dateInputContainer = new StyledFlowPanel("input-append", dateInput, dateInputAdd);
        private FlowPanel dateControl = new StyledFlowPanel("controls", dateInputContainer, dateHelp);
        private LabelWidget dateLabel = new LabelWidget("control-label", "dateInput", "Дата");
    private FlowPanel datePanel = new StyledFlowPanel("control-group", dateLabel, dateControl);

            private LabelWidget serviceHelp = new LabelWidget("help-inline", null, "");
            private MultiWordSuggestOracle serviceOracle = new MultiWordSuggestOracle();
            private SuggestBox serviceInput = new SuggestBox(serviceOracle);
        private FlowPanel serviceControl = new StyledFlowPanel("controls", serviceInput, serviceHelp);
        private LabelWidget serviceLabel = new LabelWidget("control-label", "serviceInput", "Cтатья расходов");
    private FlowPanel servicePanel = new StyledFlowPanel("control-group", serviceLabel, serviceControl);

            private LabelWidget sumToPayHelp = new LabelWidget("help-inline", null, "");
                private TextBox sumToPayInput = new TextBox();
                private SpanWidget sumToPayInputAdd = new SpanWidget("add-on", "р.");
            private FlowPanel sumToPayInputContainer = new StyledFlowPanel("input-append", sumToPayInput, sumToPayInputAdd);
        private FlowPanel sumToPayControl = new StyledFlowPanel("controls", sumToPayInputContainer, sumToPayHelp);
        private LabelWidget sumToPayLabel = new LabelWidget("control-label", "valueInput", "Сумма к оплате");
    private FlowPanel sumToPayPanel = new StyledFlowPanel("control-group", sumToPayLabel, sumToPayControl);
    
		    private LabelWidget sumPayedHelp = new LabelWidget("help-inline", null, "");
			    private TextBox sumPayedInput = new TextBox();
			    private SpanWidget sumPayedInputAdd = new SpanWidget("add-on", "р.");
		private FlowPanel sumPayedInputContainer = new StyledFlowPanel("input-append", sumPayedInput, sumPayedInputAdd);
		private FlowPanel sumPayedControl = new StyledFlowPanel("controls", sumPayedInputContainer, sumPayedHelp);
		private LabelWidget sumPayedLabel = new LabelWidget("control-label", "valueInput", "Оплаченная сумма");
	private FlowPanel sumPayedPanel = new StyledFlowPanel("control-group", sumToPayPanel, sumPayedLabel, sumPayedControl);
    
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
    private StyledFlowPanel panel = new StyledFlowPanel("ExpenseEditPanel form-horizontal", addressLegend, datePanel, servicePanel, sumPayedPanel, receiptPanel);

    private ExpenseDto object = null;

    public ExpenseEditPanel() {
        super();
        initWidget(panel);
        ChangeHandler validateHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                validate();
            }
        };

        dateInput.getElement().setId("dateInput");
        dateInput.getElement().setAttribute("placeholder", "Дата");
        dateInput.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
        dateInput.getTextBox().addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                validate();
            }
        });
        dateInput.getTextBox().addChangeHandler(validateHandler);
        dateInputAdd.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dateInput.showDatePicker();
            }
        });

        serviceInput.getTextBox().addChangeHandler(validateHandler);

        sumToPayInput.getElement().setId("sumToPayInput");
        sumToPayInput.getElement().setAttribute("placeholder", "Сумма к оплате");
        sumToPayInput.addChangeHandler(validateHandler);

        sumPayedInput.getElement().setId("sumPayedInput");
        sumPayedInput.getElement().setAttribute("placeholder", "Оплаченная сумма");
        sumPayedInput.addChangeHandler(validateHandler);

        receiptAnchor.addDeleteClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                setReceipt(null);
            }
        });

        receiptUpload.setMultiple(false);
    }

    public void setAllServices(List<String> serviceList) {
        serviceOracle.clear();
        serviceOracle.addAll(serviceList);
    }

    private void setAddress(String address) {
        addressLegend.setText(address);
    }

    private void setReceipt(FileDto receipt) {
        receiptAnchor.setFile(receipt);
        if (receipt == null) {
            receiptAnchor.getElement().getStyle().setDisplay(Display.NONE);
            receiptAnchor.getElement().getStyle().setVisibility(Visibility.HIDDEN);
        }
        else {
            receiptAnchor.getElement().getStyle().clearDisplay();
            receiptAnchor.getElement().getStyle().clearVisibility();
        }
    }

    public boolean validate() {
        boolean valid = true;

        Date date = dateInput.getValue();
        if (date == null) {
            datePanel.addStyleName("error");
            dateHelp.setText("Дата должна быть задана в формате ДД.ММ.ГГГГ");
            valid = false;
        }
        else {
            datePanel.removeStyleName("error");
            dateHelp.setText("");
        }
        double sumToPay = 0;
        try {
            sumToPay = fmt.parse(sumPayedInput.getText().replace(".", ","));
            if (sumToPay < 0) {
                sumToPayPanel.addStyleName("error");
                sumToPayHelp.setText("Сумма к оплате не должна быть отрицательной");
                valid = false;
            }
            else {
                sumToPayPanel.removeStyleName("error");
                sumToPayHelp.setText("");
            }
        }
        catch (Exception e) {
            sumToPayPanel.addStyleName("error");
            sumToPayHelp.setText("Сумма к оплате должна быть записана в формате X.XX");
            valid = false;
        }

        double sumPayed = 0;
        try {
            sumPayed = fmt.parse(sumPayedInput.getText().replace(".", ","));
            if (sumPayed < 0) {
                sumPayedPanel.addStyleName("error");
                sumPayedHelp.setText("Оплаченная сумма не должна быть отрицательной");
                valid = false;
            }
            else {
                sumPayedPanel.removeStyleName("error");
                sumPayedHelp.setText("");
            }
        }
        catch (Exception e) {
            sumPayedPanel.addStyleName("error");
            sumPayedHelp.setText("Оплаченная сумма должна быть записана в формате X.XX");
            valid = false;
        }

        return valid;
    }

    private void setObject(ExpenseDto expense) {
        if (expense == null) expense = new ExpenseDto();
        expense = expense.clone();
        this.object = expense;

        dateInput.setValue(expense.getDate());

        serviceInput.setValue(expense.getService());

        sumToPayInput.setValue(fmt.format(expense.getSumToPay()));

        sumPayedInput.setValue(fmt.format(expense.getSumPayed()));

        setReceipt(expense.getReceipt());
    }

    public ExpenseDto getObject() {
        object.setDate(dateInput.getValue());

        object.setService(serviceInput.getText());

        Double sumToPay = null;
        try {
            sumToPay = fmt.parse(sumToPayInput.getText().replace(".", ","));
        }
        catch (Exception e) {}
        object.setSumToPay(sumToPay);

        Double sumPayed = null;
        try {
            sumPayed = fmt.parse(sumPayedInput.getText().replace(".", ","));
        }
        catch (Exception e) {}
        object.setSumPayed(sumPayed);

        object.setReceipt(receiptAnchor.getFile());

        return object;
    }

    public static ExpenseEditPanel showInPopup(ExpenseDto expense, String address,
            final IPopupHandler<ExpenseDto> callback) {
        final ExpenseEditPanel editPanel = new ExpenseEditPanel();
        editPanel.setObject(expense);
        editPanel.setAddress(address);

        final DefaultModalPanel mp = new DefaultModalPanel(expense == null || expense.getId() == null, "платёж");
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
        return editPanel;
    }

}
