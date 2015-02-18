package com.land.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.land.client.ui.NavigationBar;
import com.land.client.view.DocumentsView;
import com.land.client.view.ExpensesView;
import com.land.client.view.IncomeView;
import com.land.client.view.TaxesView;
import com.land.client.view.UsersView;

public interface ClientFactory {

    EventBus getEventBus();

    PlaceController getPlaceController();

    PlaceHistoryMapper getPlaceHistoryMapper();

    GreetingServiceAsync getService();

    NavigationBar getNavigationBar();

    DocumentsView getDocumentsView();

    UsersView getUsersView();

    TaxesView getTaxesView();

    IncomeView getIncomeView();

    ExpensesView getExpensesView();

}
