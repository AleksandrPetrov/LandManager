package com.land.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.land.client.LandManager;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.form.LabelWidget;
import com.land.client.ui.form.LegendWidget;
import com.land.client.ui.form.SpanWidget;
import com.land.client.ui.form.StyledFlowPanel;
import com.land.shared.dto.IncomeDto;

public class IncomeEditPanel extends Composite {

    private NumberFormat fmt = NumberFormat.getFormat("0.00");

    private LegendWidget addressLegend = new LegendWidget();

    //@formatter:off
		    private LabelWidget yearHelp = new LabelWidget("help-inline", null, "");
		    private TextBox yearInput = new TextBox();
		private FlowPanel yearControl = new StyledFlowPanel("controls", yearInput, yearHelp);
		private LabelWidget yearLabel = new LabelWidget("control-label", "yearInput", "Год");
	private FlowPanel yearPanel = new StyledFlowPanel("control-group", yearLabel, yearControl);

		    private LabelWidget monthHelp = new LabelWidget("help-inline", null, "");
		    private ListBox monthInput = new ListBox(false);
		private FlowPanel monthControl = new StyledFlowPanel("controls", monthInput, monthHelp);
		private LabelWidget monthLabel = new LabelWidget("control-label", "yearInput", "Месяц");
	private FlowPanel monthPanel = new StyledFlowPanel("control-group", monthLabel, monthControl);

//            private LabelWidget dateHelp = new LabelWidget("help-inline", null, "");
//                private DateBox dateInput = new DateBox();
//                private SpanWidget dateInputAdd = new SpanWidget("add-on", "<i class='icon-calendar'></i>");
//            private FlowPanel dateInputContainer = new StyledFlowPanel("input-append", dateInput, dateInputAdd);
//        private FlowPanel dateControl = new StyledFlowPanel("controls", dateInputContainer, dateHelp);
//        private LabelWidget dateLabel = new LabelWidget("control-label", "dateInput", "Дата");
//    private FlowPanel datePanel = new StyledFlowPanel("control-group", dateLabel, dateControl);

            private LabelWidget valueHelp = new LabelWidget("help-inline", null, "");
                private TextBox valueInput = new TextBox();
                private SpanWidget valueInputAdd = new SpanWidget("add-on", "р.");
            private FlowPanel valueInputContainer = new StyledFlowPanel("input-append", valueInput, valueInputAdd);
        private FlowPanel valueControl = new StyledFlowPanel("controls", valueInputContainer, valueHelp);
        private LabelWidget valueLabel = new LabelWidget("control-label", "valueInput", "Сумма");
    private FlowPanel valuePanel = new StyledFlowPanel("control-group", valueLabel, valueControl);
    //@formatter:on

    private StyledFlowPanel panel = new StyledFlowPanel("IncomeEditPanel form-horizontal", addressLegend, yearPanel, monthPanel, valuePanel);

    private IncomeDto object = null;

    public IncomeEditPanel() {
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

        for (String month : LandManager.MONTHS)
            monthInput.addItem(month);
        monthInput.setSelectedIndex(0);
        monthInput.getElement().setId("monthInput");
        monthInput.addChangeHandler(validateHandler);

        valueInput.getElement().setId("valueInput");
        valueInput.getElement().setAttribute("placeholder", "Сумма");
        valueInput.addChangeHandler(validateHandler);
    }

    private void setAddress(String address) {
        addressLegend.setText(address);
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

    private void setObject(IncomeDto income) {
        if (income == null) income = new IncomeDto();
        income = income.clone();
        this.object = income;

        yearInput.setText(income.getYear() + "");
        monthInput.setSelectedIndex(income.getMonth().intValue());
        valueInput.setValue(fmt.format(income.getValue()));
    }

    public IncomeDto getObject() {
        Long year = null;
        try {
            year = Long.parseLong(yearInput.getText());
        }
        catch (Exception e) {}
        object.setYear(year);
        object.setMonth(Long.valueOf(monthInput.getSelectedIndex()));

        Double value = null;
        try {
            value = fmt.parse(valueInput.getText());
        }
        catch (Exception e) {}
        object.setValue(value);

        return object;
    }

    public static void showInPopup(IncomeDto income, String address, final IPopupHandler<IncomeDto> callback) {
        final IncomeEditPanel editPanel = new IncomeEditPanel();
        editPanel.setObject(income);
        editPanel.setAddress(address);

        final ModalPanel mp = new ModalPanel();
        if (income == null || income.getId() == null) {
            mp.setHeader("Добавить платёж");
            mp.setButtonText(EModalResult.SAVE, "Добавить");
            mp.setButtonVisible(EModalResult.DELETE, false);
        }
        else {
            mp.setHeader("Изменить платёж");
            mp.setButtonText(EModalResult.SAVE, "Сохранить");
            mp.setButtonVisible(EModalResult.DELETE, true);
        }
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
