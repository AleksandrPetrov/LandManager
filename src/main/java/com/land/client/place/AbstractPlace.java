package com.land.client.place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.land.client.mvp.AppPlaceHistoryMapper;

public abstract class AbstractPlace extends Place {

    public String toString() {
        return ((AppPlaceHistoryMapper) GWT.create(AppPlaceHistoryMapper.class)).getToken(this);
    }
}
