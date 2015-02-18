package com.land.client.ui;

import java.util.Date;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.land.client.place.DocumentsPlace;
import com.land.client.place.ExpensesPlace;
import com.land.client.place.IncomePlace;
import com.land.client.place.TaxesPlace;

@SuppressWarnings("deprecation")
public class NavigationPanel extends Composite {

    // @formatter:off
    private HTMLPanel panel = new HTMLPanel(
    	"<ul class='nav nav-list well'>" +
		    "<li class='active' id=navDocuments></li>" +
    		"<li class='nav-header'>Платежи</li>" +
		    "<li id=navTaxes></li>" +
		    "<li id=naxExpenses></li>" +
		    "<li id=navIncome></li>" +
        "</ul>");
    // @formatter:on

    public NavigationPanel() {
        super();
        initWidget(panel);
        panel.add(new InlineHyperlink("Документы", new DocumentsPlace(1L).toString()), "navDocuments");
        panel.add(new InlineHyperlink("Налоги", new TaxesPlace(1L).toString()), "navTaxes");
        panel.add(new InlineHyperlink("Расходы", new ExpensesPlace(new Date().getYear() + 1900L, 1L).toString()), "naxExpenses");
        panel.add(new InlineHyperlink("Доход", new IncomePlace(new Date().getYear() + 1900L, 1L).toString()), "navIncome");
    }

    public void setActivePlace(Place place) {
        panel.getElementById("navDocuments").removeClassName("active");
        panel.getElementById("navTaxes").removeClassName("active");
        panel.getElementById("naxExpenses").removeClassName("active");
        panel.getElementById("navIncome").removeClassName("active");

        Element el = getListItemByPlace(place);
        if (el != null) el.addClassName("active");
    }

    private Element getListItemByPlace(Place place) {
        String id = null;

        if (place instanceof DocumentsPlace)
            id = "navDocuments";
        else if (place instanceof TaxesPlace)
            id = "navTaxes";
        else if (place instanceof ExpensesPlace)
            id = "naxExpenses";
        else if (place instanceof IncomePlace) id = "navIncome";

        if (id == null)
            return null;
        else
            return panel.getElementById(id);
    }

}
