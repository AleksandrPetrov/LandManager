package com.land.client.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.land.client.ClientFactory;
import com.land.client.SimpleAsyncCallback;
import com.land.client.place.UsersPlace;
import com.land.client.ui.ConfirmModalPanel;
import com.land.client.ui.IPopupHandler;
import com.land.client.ui.ModalPanel;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.UserEditAdminPanel;
import com.land.client.ui.WaitPanel;
import com.land.client.view.UsersView;
import com.land.shared.dto.UserDto;
import com.land.shared.dto.qbe.UserQBE;

public class UsersActivity extends AbstractActivity implements UsersView.Presenter {

    private final static Long ITEMS_PER_PAGE = 20L;

    private ClientFactory clientFactory;
    private UsersPlace place;

    public UsersActivity(UsersPlace place, ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
        this.place = place;
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        clientFactory.getNavigationBar().setActivePlace(place);
        final UsersView view = clientFactory.getUsersView();
        view.setPresenter(this);
        panel.setWidget(view.asWidget());

        refreshPages();
    }

    private void refreshPages() {
        WaitPanel.show();
        clientFactory.getUsersView().setPageCount(0L);
        clientFactory.getUsersView().setUsers(new ArrayList<UserDto>());

        clientFactory.getService().getUsersCount(new UserQBE(), new SimpleAsyncCallback<Long>() {
            public void onSuccess(Long result) {
                clientFactory.getUsersView().setPageCount(getPageCount(result));
                clientFactory.getUsersView().setCurrentPage(place.getPage());

                refreshList();
            }
        });
    }

    private void refreshList() {
        WaitPanel.show();
        clientFactory.getUsersView().setUsers(new ArrayList<UserDto>());

        UserQBE qbe = new UserQBE(getFirstIndex(place.getPage()), ITEMS_PER_PAGE);
        clientFactory.getService().getUsers(qbe, new SimpleAsyncCallback<List<UserDto>>() {
            public void onSuccess(List<UserDto> result) {
                clientFactory.getUsersView().setUsers(result);
                WaitPanel.hide();
            }
        });
    }

    public void onAddObjectClick() {
        UserDto objectDto = new UserDto();
        showEditPopup(objectDto);
    }

    public void onObjectClick(UserDto userDto) {
        showEditPopup(userDto);
    }

    private void showEditPopup(final UserDto object) {
        UserEditAdminPanel.showInPopup(object, new IPopupHandler<UserDto>() {
            public void onSave(final ModalPanel mp, final UserDto item) {
                WaitPanel.show();
                clientFactory.getService().saveUserByAdmin(item, new SimpleAsyncCallback<Long>() {
                    public void onSuccess(Long result) {
                        if (item.getId() == null)
                            refreshPages();
                        else
                            refreshList();
                        mp.hide();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        WaitPanel.hide();
                        UserEditAdminPanel panel = (UserEditAdminPanel) mp.getContent();
                        panel.setLoginHelpText(caught.getMessage());
                    }
                });

            }

            public void onDelete(ModalPanel mp, UserDto item) {
                onObjectDelete(item);
                mp.hide();
            }

            public void onCancel(ModalPanel mp) {
                mp.hide();
            }
        });
    }

    public void onObjectSave(final UserDto item) {

    }

    public void onObjectDelete(final UserDto item) {
        ConfirmModalPanel mp = new ConfirmModalPanel("Подтверждение", "Удалить пользователя: " + item.getName() + "?", "Удалить");
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.DELETE) {
                    WaitPanel.show();
                    clientFactory.getService().deleteUser(item.getId(), new SimpleAsyncCallback<Void>() {
                        public void onSuccess(Void result) {
                            refreshPages();
                        }
                    });
                }
                return true;
            }
        });
        mp.show();
    }

    public void onPagerChange(Long newPage) {
        place.setPage(newPage);
        History.newItem(place.toString(), false);
        refreshList();

    }

    private Long getPageCount(Long itemsCount) {
        return (itemsCount % ITEMS_PER_PAGE >= 1 ? itemsCount / ITEMS_PER_PAGE + 1 : itemsCount / ITEMS_PER_PAGE);
    }

    private Long getFirstIndex(Long newPage) {
        return (newPage - 1) * ITEMS_PER_PAGE;
    }

}
