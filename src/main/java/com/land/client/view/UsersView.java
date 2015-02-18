package com.land.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.land.shared.dto.UserDto;

public interface UsersView extends IsWidget {

    void setPageCount(Long pageCount);

    void setCurrentPage(Long currentPage);

    void setUsers(List<UserDto> listUsers);

    void setPresenter(Presenter presenter);

    public interface Presenter {

        void onAddObjectClick();

        void onPagerChange(Long newPage);

        void onObjectClick(UserDto userDto);
    }

}
