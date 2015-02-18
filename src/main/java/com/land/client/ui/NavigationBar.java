package com.land.client.ui;

import java.util.Date;

import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.land.client.LandManager;
import com.land.client.place.DocumentsPlace;
import com.land.client.place.ExpensesPlace;
import com.land.client.place.IncomePlace;
import com.land.client.place.TaxesPlace;
import com.land.client.place.UsersPlace;
import com.land.shared.dto.UserDto;

@SuppressWarnings("deprecation")
public class NavigationBar extends Composite {

    private Anchor currentUserWidget = null;

    // @formatter:off
    private HTMLPanel panel = new HTMLPanel(
        "<div class='navbar-inner'>" +
            "<div class='container-fluid'>" +
            "<ul class='nav'>" +
                "<li class='active' id=navDocuments></li>" +
                /*"<li class='nav-header'>Платежи</li>" +*/
                "<li id=navTaxes></li>" +
                "<li id=naxExpenses></li>" +
                "<li id=navIncome></li>" +
                "<li id=navUsers></li>" +
            "</ul>" +
                /*"<a class='brand' href='#'>Тут должно быть название</a>" +*/
                "<span class='gc-social pull-right'>" +
                    "<div>" +
                      "<div id='navCurrentUser' class='email'></div>" +
                      "<a href='logout'>Выход</a>" +
                    "</div>" +
                    "<img src='/img/user.png'>" +
                "</span>" +
            "</div>" + 
        "</div>");
    // @formatter:on
    public NavigationBar() {
        initWidget(panel);
        panel.setStyleName("navbar navbar-fixed-top");
        panel.add(new InlineHyperlink("Документы", new DocumentsPlace(1L).toString()), "navDocuments");
        panel.add(new InlineHyperlink("Налоги", new TaxesPlace(1L).toString()), "navTaxes");
        panel.add(new InlineHyperlink("Расходы", new ExpensesPlace(new Date().getYear() + 1900L, 1L).toString()), "naxExpenses");
        panel.add(new InlineHyperlink("Доходы", new IncomePlace(new Date().getYear() + 1900L, 1L).toString()), "navIncome");
        panel.add(new InlineHyperlink("Пользователи", new UsersPlace(1L).toString()), "navUsers");
    }

    public void setCurrentUser(final UserDto user) {
        if (currentUserWidget != null) currentUserWidget.removeFromParent();
        currentUserWidget = new Anchor(user.getName());
        currentUserWidget.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                LandManager.onCurrentUserClick();
            }
        });
        panel.add(currentUserWidget, "navCurrentUser");
        panel.getElementById("navUsers").getStyle().setVisibility(user.getAdmin() ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    public void setActivePlace(Place place) {
        panel.getElementById("navDocuments").removeClassName("active");
        panel.getElementById("navTaxes").removeClassName("active");
        panel.getElementById("naxExpenses").removeClassName("active");
        panel.getElementById("navIncome").removeClassName("active");
        panel.getElementById("navUsers").removeClassName("active");

        Element el = getListItemByPlace(place);
        if (el != null) el.addClassName("active");
    }

    public void setVisible(Place place, boolean visible) {
        getListItemByPlace(place).getStyle().setVisibility(visible ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    private Element getListItemByPlace(Place place) {
        String id = null;

        if (place instanceof DocumentsPlace)
            id = "navDocuments";
        else if (place instanceof TaxesPlace)
            id = "navTaxes";
        else if (place instanceof ExpensesPlace)
            id = "naxExpenses";
        else if (place instanceof IncomePlace)
            id = "navIncome";
        else if (place instanceof UsersPlace) id = "navUsers";

        if (id == null)
            return null;
        else
            return panel.getElementById(id);
    }

}
