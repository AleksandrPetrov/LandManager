package com.land.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.land.client.mvp.AppPlaceHistoryMapper;
import com.land.client.ui.NavigationBar;
import com.land.client.view.DocumentsView;
import com.land.client.view.DocumentsViewImpl;
import com.land.client.view.ExpensesView;
import com.land.client.view.ExpensesViewImpl;
import com.land.client.view.IncomeView;
import com.land.client.view.IncomeViewImpl;
import com.land.client.view.TaxesView;
import com.land.client.view.TaxesViewImpl;
import com.land.client.view.UsersView;
import com.land.client.view.UsersViewImpl;

public class ClientFactoryImpl implements ClientFactory {
    private final EventBus eventBus = new SimpleEventBus();
    @SuppressWarnings("deprecation")
    private final PlaceController placeController = new PlaceController(eventBus);
    private final PlaceHistoryMapper placeHistoryMapper = GWT.create(AppPlaceHistoryMapper.class);
    private final GreetingServiceAsync service = GWT.create(GreetingService.class);

    private final NavigationBar navigationBar = new NavigationBar();

    private final DocumentsView documentsView = new DocumentsViewImpl();
    private final TaxesView taxesView = new TaxesViewImpl();
    private final IncomeView incomeView = new IncomeViewImpl();
    private final ExpensesView expensesView = new ExpensesViewImpl();
    private final UsersView usersView = new UsersViewImpl();

    public EventBus getEventBus() {
        return eventBus;
    }

    public PlaceController getPlaceController() {
        return placeController;
    }

    public PlaceHistoryMapper getPlaceHistoryMapper() {
        return placeHistoryMapper;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public DocumentsView getDocumentsView() {
        return documentsView;
    }

    public GreetingServiceAsync getService() {
        return service;
    }

    public TaxesView getTaxesView() {
        return taxesView;
    }

    public IncomeView getIncomeView() {
        return incomeView;
    }

    public ExpensesView getExpensesView() {
        return expensesView;
    }

    public UsersView getUsersView() {
        return usersView;
    }
}
