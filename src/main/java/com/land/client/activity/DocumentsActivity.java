package com.land.client.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.land.client.ClientFactory;
import com.land.client.SimpleAsyncCallback;
import com.land.client.place.DocumentsPlace;
import com.land.client.ui.ConfirmModalPanel;
import com.land.client.ui.IPopupHandler;
import com.land.client.ui.ModalPanel;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.ObjectEditPanel;
import com.land.client.ui.WaitPanel;
import com.land.client.view.DocumentsView;
import com.land.shared.dto.FileDto;
import com.land.shared.dto.ObjectDocumentsItemDto;
import com.land.shared.dto.ObjectDto;
import com.land.shared.dto.qbe.ObjectQBE;

public class DocumentsActivity extends AbstractActivity implements DocumentsView.Presenter {

    private final static Long ITEMS_PER_PAGE = 20L;

    private ClientFactory clientFactory;
    private DocumentsPlace place;

    public DocumentsActivity(DocumentsPlace place, ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
        this.place = place;
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        clientFactory.getNavigationBar().setActivePlace(place);
        final DocumentsView view = clientFactory.getDocumentsView();
        view.setPresenter(this);
        panel.setWidget(view.asWidget());

        refreshPages();
    }

    private void refreshPages() {
        WaitPanel.show();
        clientFactory.getDocumentsView().setPageCount(0L);
        clientFactory.getDocumentsView().setObjectDocuments(new ArrayList<ObjectDocumentsItemDto>());

        clientFactory.getService().getObjectDocumentsCount(new ObjectQBE(), new SimpleAsyncCallback<Long>() {
            public void onSuccess(Long result) {
                clientFactory.getDocumentsView().setPageCount(getPageCount(result));
                clientFactory.getDocumentsView().setCurrentPage(place.getPage());

                refreshList();
            }
        });
    }

    private void refreshList() {
        WaitPanel.show();
        clientFactory.getDocumentsView().setObjectDocuments(new ArrayList<ObjectDocumentsItemDto>());

        ObjectQBE qbe = new ObjectQBE(getFirstIndex(place.getPage()), ITEMS_PER_PAGE);
        clientFactory.getService().getObjectDocuments(qbe, new SimpleAsyncCallback<List<ObjectDocumentsItemDto>>() {
            public void onSuccess(List<ObjectDocumentsItemDto> result) {
                clientFactory.getDocumentsView().setObjectDocuments(result);
                WaitPanel.hide();
            }
        });
    }

    public void onAddObjectClick() {
        ObjectDto objectDto = new ObjectDto();
        showEditPopup(objectDto);
    }

    public void onObjectClick(ObjectDocumentsItemDto objectDocumentsItemDto) {
        showEditPopup(objectDocumentsItemDto);
    }

    private void showEditPopup(final ObjectDto object) {
        ObjectEditPanel.showInPopup(object, new IPopupHandler<ObjectDto>() {
            public void onSave(ModalPanel mp, ObjectDto item) {
                onObjectSave(item);
            }

            public void onDelete(ModalPanel mp, ObjectDto item) {
                onObjectDelete(item);
            }

            public void onCancel(ModalPanel mp) {}
        });
    }

    public void onObjectSave(final ObjectDto item) {
        WaitPanel.show();
        clientFactory.getService().saveObject(item, new SimpleAsyncCallback<Long>() {
            public void onSuccess(Long result) {
                if (item.getId() == null)
                    refreshPages();
                else
                    refreshList();
            }
        });
    }

    public void onObjectDelete(final ObjectDto item) {
        ConfirmModalPanel mp = new ConfirmModalPanel("Подтверждение", "Удалить объект: " + item.getAddress() + "?", "Удалить");
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.DELETE) {
                    WaitPanel.show();
                    clientFactory.getService().deleteObject(item.getId(), new SimpleAsyncCallback<Void>() {
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

    public void onFileDelete(final FileDto fileDto) {
        ConfirmModalPanel mp = new ConfirmModalPanel("Подтверждение", "Удалить документ: " + fileDto.getName() + "?", "Удалить");
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.DELETE) {
                    WaitPanel.show();
                    clientFactory.getService().deleteFile(fileDto.getId(), new SimpleAsyncCallback<Void>() {
                        public void onSuccess(Void result) {
                            clientFactory.getDocumentsView().removeFile(fileDto);
                            WaitPanel.hide();
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
