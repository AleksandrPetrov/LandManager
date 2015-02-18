package com.land.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.land.client.ClientFactory;
import com.land.client.LandManager;
import com.land.client.SimpleAsyncCallback;
import com.land.client.place.IncomePlace;
import com.land.client.ui.ConfirmModalPanel;
import com.land.client.ui.IPopupHandler;
import com.land.client.ui.IncomeEditPanel;
import com.land.client.ui.ModalPanel;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.WaitPanel;
import com.land.client.view.IncomeView;
import com.land.shared.dto.AggregatedIncomeDto;
import com.land.shared.dto.IncomeDto;
import com.land.shared.dto.ObjectIncomesItemDto;
import com.land.shared.dto.ObjectYearsBoundariesDto;
import com.land.shared.dto.qbe.AggregatedIncomesQBE;
import com.land.shared.dto.qbe.ObjectIncomeQBE;
import com.land.shared.dto.qbe.ObjectQBE;

public class IncomeActivity extends AbstractActivity implements IncomeView.Presenter {

    private final static Long ITEMS_PER_PAGE = 20L;

    private ClientFactory clientFactory;
    private IncomePlace place;

    public IncomeActivity(IncomePlace place, ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
        this.place = place;
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        clientFactory.getNavigationBar().setActivePlace(place);
        final IncomeView view = clientFactory.getIncomeView();
        view.setPresenter(this);
        panel.setWidget(view.asWidget());

        refreshYears();
    }

    private void refreshYears() {
        WaitPanel.show();
        clientFactory.getIncomeView().setYearBoundaries(0L, -1L);
        clientFactory.getIncomeView().setPageCount(0L);
        clientFactory.getIncomeView().setObjectIncomes(new ArrayList<ObjectIncomesItemDto>());

        ObjectQBE qbe = new ObjectQBE();
        qbe.setIncomesView(true);
        clientFactory.getService().getObjectIncomeBoundaries(qbe, new SimpleAsyncCallback<ObjectYearsBoundariesDto>() {
            public void onSuccess(ObjectYearsBoundariesDto result) {
                clientFactory.getIncomeView().setYearBoundaries(result.getMinYear(), result.getMaxYear());
                clientFactory.getIncomeView().setCurrentYear(place.getYear());

                clientFactory.getIncomeView().setPageCount(getPageCount(result.getObjectCount()));
                clientFactory.getIncomeView().setCurrentPage(place.getPage());

                refreshList();
                refreshTotal();
            }
        });
    }

    private void refreshTotal() {
        clientFactory.getIncomeView().setTotalObjectAggredatedList(new ArrayList<AggregatedIncomeDto>());
        AggregatedIncomesQBE qbe = new AggregatedIncomesQBE(getFirstIndex(place.getPage()), ITEMS_PER_PAGE);
        qbe.setYear(place.getYear());
        clientFactory.getService().getTotalObjectAggregatedIncomes(qbe, new SimpleAsyncCallback<List<AggregatedIncomeDto>>() {
            public void onSuccess(List<AggregatedIncomeDto> result) {
                clientFactory.getIncomeView().setTotalObjectAggredatedList(result);
            }
        });
    }

    private void refreshList() {
        WaitPanel.show();
        clientFactory.getIncomeView().setObjectIncomes(new ArrayList<ObjectIncomesItemDto>());

        ObjectIncomeQBE qbe = new ObjectIncomeQBE(getFirstIndex(place.getPage()), ITEMS_PER_PAGE);
        qbe.setYear(place.getYear());
        clientFactory.getService().getObjectIncomes(qbe, new SimpleAsyncCallback<List<ObjectIncomesItemDto>>() {
            public void onSuccess(List<ObjectIncomesItemDto> result) {
                clientFactory.getIncomeView().setObjectIncomes(result);
                WaitPanel.hide();
            }
        });
    }

    public void onIncomeClick(ObjectIncomesItemDto item, IncomeDto income) {
        showEditPopup(income, item.getLandObject().getAddress());
    }

    @SuppressWarnings("deprecation")
    public void onAddIncomeClick(ObjectIncomesItemDto item) {
        IncomeDto tax = new IncomeDto();
        tax.setObjectId(item.getLandObject().getId());
        tax.setValue(0d);
        tax.setYear(Long.valueOf(new Date().getYear() + 1900));
        tax.setMonth(Long.valueOf(new Date().getMonth()));

        showEditPopup(tax, item.getLandObject().getAddress());
    }

    private void showEditPopup(IncomeDto tax, String address) {
        IncomeEditPanel.showInPopup(tax, address, new IPopupHandler<IncomeDto>() {
            public void onSave(ModalPanel mp, IncomeDto item) {
                onIncomeSave(item);
            }

            public void onDelete(ModalPanel mp, IncomeDto item) {
                onIncomeDelete(item);
            }

            public void onCancel(ModalPanel mp) {}
        });
    }

    protected void onIncomeDelete(final IncomeDto item) {
        ConfirmModalPanel mp = new ConfirmModalPanel("Подтверждение", "Удалить доходы за "
                + LandManager.MONTHS[item.getMonth().intValue()].toLowerCase() + " ?", "Удалить");
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.DELETE) {
                    WaitPanel.show();
                    clientFactory.getService().deleteIncome(item.getId(), new SimpleAsyncCallback<Void>() {
                        public void onSuccess(Void result) {
                            refreshYears();
                        }
                    });
                }
                return true;
            }
        });
        mp.show();
    }

    protected void onIncomeSave(IncomeDto item) {
        WaitPanel.show();
        clientFactory.getService().saveIncome(item, new SimpleAsyncCallback<Long>() {
            public void onSuccess(Long id) {
                refreshYears();
            }
        });
    }

    public void onPagerChange(Long newPage) {
        place.setPage(newPage);
        History.newItem(place.toString(), false);
        refreshList();
    }

    public void onYearPagerChange(Long newPage) {
        place.setYear(newPage);
        place.setPage(place.getPage());
        History.newItem(place.toString(), false);
        refreshList();
        refreshTotal();
    }

    private Long getPageCount(Long itemsCount) {
        return (itemsCount % ITEMS_PER_PAGE >= 1 ? itemsCount / ITEMS_PER_PAGE + 1 : itemsCount / ITEMS_PER_PAGE);
    }

    private Long getFirstIndex(Long newPage) {
        return (newPage - 1) * ITEMS_PER_PAGE;
    }

}
