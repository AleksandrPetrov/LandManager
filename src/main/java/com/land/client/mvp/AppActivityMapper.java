package com.land.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.land.client.ClientFactory;
import com.land.client.activity.DocumentsActivity;
import com.land.client.activity.ExpensesActivity;
import com.land.client.activity.IncomeActivity;
import com.land.client.activity.TaxesActivity;
import com.land.client.activity.UsersActivity;
import com.land.client.place.DocumentsPlace;
import com.land.client.place.ExpensesPlace;
import com.land.client.place.IncomePlace;
import com.land.client.place.TaxesPlace;
import com.land.client.place.UsersPlace;

public class AppActivityMapper implements ActivityMapper {
    private ClientFactory clientFactory;

    public AppActivityMapper(ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
    }

    public Activity getActivity(Place place) {
        if (place instanceof DocumentsPlace) return new DocumentsActivity((DocumentsPlace) place, clientFactory);
        if (place instanceof ExpensesPlace) return new ExpensesActivity((ExpensesPlace) place, clientFactory);
        if (place instanceof IncomePlace) return new IncomeActivity((IncomePlace) place, clientFactory);
        if (place instanceof TaxesPlace) return new TaxesActivity((TaxesPlace) place, clientFactory);
        if (place instanceof UsersPlace) return new UsersActivity((UsersPlace) place, clientFactory);
        return null;
    }
}
