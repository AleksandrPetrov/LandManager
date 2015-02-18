package com.land.client.mvp;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.land.client.place.DocumentsPlace;
import com.land.client.place.ExpensesPlace;
import com.land.client.place.IncomePlace;
import com.land.client.place.TaxesPlace;
import com.land.client.place.UsersPlace;

@WithTokenizers({DocumentsPlace.Tokenizer.class, ExpensesPlace.Tokenizer.class, IncomePlace.Tokenizer.class,
        TaxesPlace.Tokenizer.class, UsersPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {}
