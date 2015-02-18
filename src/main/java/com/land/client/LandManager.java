package com.land.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.land.client.mvp.AppActivityMapper;
import com.land.client.place.DocumentsPlace;
import com.land.client.ui.IPopupHandler;
import com.land.client.ui.ModalPanel;
import com.land.client.ui.UserEditPanel;
import com.land.client.ui.WaitPanel;
import com.land.shared.dto.UserDto;

public class LandManager implements EntryPoint {

    public final static String[] MONTHS = new String[] {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль",
            "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    public static ClientFactory clientFactory = GWT.create(ClientFactory.class);
    private static UserDto currentUser = null;

    private SimplePanel content = new SimplePanel();

    public void onModuleLoad() {
        clientFactory.getService().getCurrentUser(new SimpleAsyncCallback<UserDto>() {
            public void onSuccess(UserDto user) {
                currentUser = user;
                initUserPlaceHistoryHandler();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void initUserPlaceHistoryHandler() {
        // ClientFactory clientFactory = GWT.create(ClientFactory.class);
        EventBus eventBus = clientFactory.getEventBus();
        PlaceController placeController = clientFactory.getPlaceController();

        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(content);

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(clientFactory.getPlaceHistoryMapper());
        historyHandler.register(placeController, eventBus, new DocumentsPlace(1L));

        // Добавляем интерфейс
        renderInterface(clientFactory);
        // Goes to the place represented on URL else default place
        historyHandler.handleCurrentHistory();

        Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                setLeft(".window-scroll-aware", Window.getScrollLeft());
            }
        });
        Window.addWindowScrollHandler(new ScrollHandler() {
            public void onWindowScroll(ScrollEvent event) {
                setLeft(".window-scroll-aware", Window.getScrollLeft());
            }
        });
    }

    public native static void setLeft(String selector, int newLeft) /*-{
		if (newLeft <= 30)
			newLeft = -8;
		else
			newLeft = newLeft - 30 + 8;
		$wnd.$(selector).attr('style', "left:" + newLeft + "px");
    }-*/;

    private void renderInterface(ClientFactory clientFactory) {
        Element loadingElement = DOM.getElementById("loading");
        if (loadingElement != null && loadingElement.getParentElement() != null) loadingElement.removeFromParent();

        RootPanel.get().add(clientFactory.getNavigationBar());
        clientFactory.getNavigationBar().setCurrentUser(currentUser);
        RootPanel.get().add(content);
        content.setStyleName("container-fluid");
    }

    public static void onCurrentUserClick() {
        UserEditPanel.showInPopup(currentUser, new IPopupHandler<UserDto>() {
            public void onSave(final ModalPanel mp, final UserDto item) {
                WaitPanel.show();
                clientFactory.getService().saveUser(item, new SimpleAsyncCallback<Long>() {
                    public void onSuccess(Long id) {
                        WaitPanel.hide();
                        currentUser = item;
                        clientFactory.getNavigationBar().setCurrentUser(currentUser);
                        mp.hide();
                    }

                    public void onFailure(Throwable caught) {
                        WaitPanel.hide();
                        UserEditPanel panel = (UserEditPanel) mp.getContent();
                        panel.setPasswordOldHelpText(caught.getMessage());
                    }
                });
            }

            public void onDelete(ModalPanel mp, UserDto item) {
                // этой кнопки вообще нет
            }

            public void onCancel(ModalPanel mp) {
                mp.hide();
            }
        });
    }

    public static void onCurrentUserSave(final UserDto item) {
        WaitPanel.show();

    }
}
