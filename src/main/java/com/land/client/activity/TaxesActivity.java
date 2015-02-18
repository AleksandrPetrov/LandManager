package com.land.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.land.client.ClientFactory;
import com.land.client.SimpleAsyncCallback;
import com.land.client.place.TaxesPlace;
import com.land.client.ui.ConfirmModalPanel;
import com.land.client.ui.IPopupHandler;
import com.land.client.ui.ModalPanel;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.TaxEditPanel;
import com.land.client.ui.WaitPanel;
import com.land.client.view.TaxesView;
import com.land.shared.dto.AggregatedTaxDto;
import com.land.shared.dto.ObjectTaxesItemDto;
import com.land.shared.dto.TaxDto;
import com.land.shared.dto.qbe.ObjectQBE;

public class TaxesActivity extends AbstractActivity implements TaxesView.Presenter {

    private final static Long ITEMS_PER_PAGE = 20L;

    private ClientFactory clientFactory;
    private TaxesPlace place;

    private List<ObjectTaxesItemDto> taxes = new ArrayList<ObjectTaxesItemDto>();

    public TaxesActivity(TaxesPlace place, ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
        this.place = place;
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        clientFactory.getNavigationBar().setActivePlace(place);
        final TaxesView view = clientFactory.getTaxesView();
        view.setPresenter(this);
        taxes.clear();
        panel.setWidget(view.asWidget());

        refreshTotal();
    }

    private void refreshPages() {
        WaitPanel.show();
        clientFactory.getTaxesView().setPageCount(0L);
        clientFactory.getTaxesView().setObjectTaxes(new ArrayList<ObjectTaxesItemDto>());

        ObjectQBE qbe = new ObjectQBE();
        qbe.setTaxesView(true);
        clientFactory.getService().getObjectTaxesCount(qbe, new SimpleAsyncCallback<Long>() {
            public void onSuccess(Long result) {
                clientFactory.getTaxesView().setPageCount(getPageCount(result));
                clientFactory.getTaxesView().setCurrentPage(place.getPage());

                refreshList();
            }
        });
    }

    private void refreshTotal() {
        WaitPanel.show();
        clientFactory.getTaxesView().setTotalObjectAggredatedList(new ArrayList<AggregatedTaxDto>());
        clientFactory.getService().getTotalObjectAggregatedTaxes(new SimpleAsyncCallback<List<AggregatedTaxDto>>() {
            public void onSuccess(List<AggregatedTaxDto> result) {
                clientFactory.getTaxesView().setTotalObjectAggredatedList(result);
                refreshPages();
            }
        });
    }

    private void refreshList() {
        WaitPanel.show();
        clientFactory.getTaxesView().setObjectTaxes(new ArrayList<ObjectTaxesItemDto>());

        ObjectQBE qbe = new ObjectQBE(getFirstIndex(place.getPage()), ITEMS_PER_PAGE);
        qbe.setTaxesView(true);
        clientFactory.getService().getObjectTaxes(qbe, new SimpleAsyncCallback<List<ObjectTaxesItemDto>>() {
            public void onSuccess(List<ObjectTaxesItemDto> result) {
                taxes = result;
                clientFactory.getTaxesView().setObjectTaxes(result);
                WaitPanel.hide();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void onAddTaxClick(ObjectTaxesItemDto item) {
        TaxDto tax = new TaxDto();
        tax.setObjectId(item.getLandObject().getId());
        tax.setValue(0d);
        tax.setYear(new Date().getYear() + 1900);

        showEditPopup(tax, item.getLandObject().getAddress());
    }

    public void onTaxClick(ObjectTaxesItemDto item, TaxDto tax) {
        showEditPopup(tax, item.getLandObject().getAddress());
    }

    private void showEditPopup(TaxDto tax, String address) {
        TaxEditPanel.showInPopup(tax, address, new IPopupHandler<TaxDto>() {
            public void onSave(ModalPanel mp, TaxDto item) {
                onTaxSave(item);
            }

            public void onDelete(ModalPanel mp, TaxDto item) {
                onTaxDelete(item);
            }

            public void onCancel(ModalPanel mp) {}
        });
    }

    public void onTaxSave(final TaxDto item) {
        WaitPanel.show();
        clientFactory.getService().saveTax(item, new SimpleAsyncCallback<Long>() {
            public void onSuccess(Long id) {
                refreshTotal();
            }
        });
    }

    public void onTaxDelete(final TaxDto item) {
        ConfirmModalPanel mp = new ConfirmModalPanel("Подтверждение", "Удалить налог за " + item.getYear() + " год?", "Удалить");
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.DELETE) {
                    WaitPanel.show();
                    clientFactory.getService().deleteTax(item.getId(), new SimpleAsyncCallback<Void>() {
                        public void onSuccess(Void result) {
                            refreshTotal();
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
