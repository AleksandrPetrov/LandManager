package com.land.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.land.client.ui.FileWDelAnchor;
import com.land.client.ui.Pager;
import com.land.shared.dto.UserDto;

public class UsersViewImpl extends Composite implements UsersView {

    private UsersView.Presenter presenter = null;

    private FlowPanel viewPanel = new FlowPanel();
    private FlexTable table = new FlexTable();
    private Pager pager = new Pager(Long.valueOf(10));
    private Button addObjectButton = new Button("<i class='icon-plus'></i> Добавить пользователя");

    private Map<Long, FileWDelAnchor> mapFiles = new HashMap<Long, FileWDelAnchor>();

    public UsersViewImpl() {
        super();
        viewPanel.setStyleName("UsersView");
        initWidget(viewPanel);
        table.setStyleName("table table-striped table-bordered");
        viewPanel.add(table);
        pager.addStyleName("pull-right");
        viewPanel.add(pager);

        addObjectButton.addStyleName("btn btn-primary");
        viewPanel.add(addObjectButton);

        Element thead = DOM.createTHead();
        table.getElement().insertAfter(thead, table.getElement().getFirstChild());

        Element tr = DOM.createTR();
        thead.appendChild(tr);

        Element thObject = DOM.createTH();
        thObject.setInnerText("Логин");
        tr.appendChild(thObject);

        Element thDocuments = DOM.createTH();
        thDocuments.setInnerText("Имя");
        tr.appendChild(thDocuments);
        tr.getFirstChildElement().setClassName("col1");

        addObjectButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                presenter.onAddObjectClick();
            }
        });

        pager.setChangeHandler(new Pager.IChangeHandler() {
            public void onChange(Long newPage) {
                presenter.onPagerChange(newPage);
            }
        });

    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setPageCount(Long pageCount) {
        pager.setPageCount(pageCount);
    }

    public void setCurrentPage(Long currentPage) {
        pager.setCurrentPage(currentPage);
    }

    public void setUsers(List<UserDto> listUsers) {
        table.clear();
        mapFiles.clear();
        int i = 0;
        for (final UserDto userDto : listUsers) {
            Anchor objectAnchor = new Anchor(userDto.getLogin());
            objectAnchor.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    presenter.onObjectClick(userDto);
                }
            });
            table.setWidget(i, 0, objectAnchor);
            table.getCellFormatter().setStyleName(i, 0, "col1");
            table.getRowFormatter().setStyleName(i, "row-begin");

            Anchor nameAnchor = new Anchor(userDto.getName());
            nameAnchor.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    presenter.onObjectClick(userDto);
                }
            });
            table.setWidget(i, 1, nameAnchor);

            i++;
        }
    }

}
