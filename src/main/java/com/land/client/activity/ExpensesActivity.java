package com.land.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.land.client.ClientFactory;
import com.land.client.LandManager;
import com.land.client.SimpleAsyncCallback;
import com.land.client.place.ExpensesPlace;
import com.land.client.ui.ConfirmModalPanel;
import com.land.client.ui.ExpenseEditPanel;
import com.land.client.ui.IPopupHandler;
import com.land.client.ui.ModalPanel;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.WaitPanel;
import com.land.client.view.ExpensesView;
import com.land.shared.dto.AggregatedExpensesDto;
import com.land.shared.dto.ExpenseDto;
import com.land.shared.dto.ObjectDto;
import com.land.shared.dto.ObjectExpensesItemDto;
import com.land.shared.dto.ObjectYearsBoundariesDto;
import com.land.shared.dto.qbe.AggregatedExpensesQBE;
import com.land.shared.dto.qbe.ExpenseQBE;
import com.land.shared.dto.qbe.ObjectQBE;

public class ExpensesActivity extends AbstractActivity implements ExpensesView.Presenter {

    private final static Long ITEMS_PER_PAGE = 20L;

    private ClientFactory clientFactory;
    private ExpensesPlace place;
    private Map<Long, ObjectExpensesItemDto> mapObjectIdToObjectAggregated = new LinkedHashMap<Long, ObjectExpensesItemDto>();

    public ExpensesActivity(ExpensesPlace place, ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
        this.place = place;
    }

    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        clientFactory.getNavigationBar().setActivePlace(place);
        final ExpensesView view = clientFactory.getExpensesView();
        view.setPresenter(this);
        panel.setWidget(view.asWidget());

        refreshYears();
    }

    private void refreshYears() {
        WaitPanel.show();
        clientFactory.getExpensesView().setYearBoundaries(0L, -1L);
        clientFactory.getExpensesView().setPageCount(0L);
        clientFactory.getExpensesView().setTotalObjectAggredatedList(new ArrayList<AggregatedExpensesDto>());
        // mapObjectIdToObjectAggregated.clear();

        ObjectQBE qbe = new ObjectQBE();
        qbe.setExpencesView(true);
        clientFactory.getService().getObjectExpenseBoundaries(qbe, new SimpleAsyncCallback<ObjectYearsBoundariesDto>() {
            public void onSuccess(ObjectYearsBoundariesDto result) {
                clientFactory.getExpensesView().setYearBoundaries(result.getMinYear(), result.getMaxYear());
                clientFactory.getExpensesView().setCurrentYear(place.getYear());

                clientFactory.getExpensesView().setPageCount(getPageCount(result.getObjectCount()));
                clientFactory.getExpensesView().setCurrentPage(place.getPage());

                refreshList();
                refreshTotal();
            }
        });
    }

    private void refreshTotal() {
        clientFactory.getExpensesView().setTotalObjectAggredatedList(new ArrayList<AggregatedExpensesDto>());
        AggregatedExpensesQBE qbe = new AggregatedExpensesQBE(getFirstIndex(place.getPage()), ITEMS_PER_PAGE);
        qbe.setYear(place.getYear());
        clientFactory.getService().getTotalObjectAggregatedExpenses(qbe, new SimpleAsyncCallback<List<AggregatedExpensesDto>>() {
            public void onSuccess(List<AggregatedExpensesDto> result) {
                clientFactory.getExpensesView().setTotalObjectAggredatedList(result);
            }
        });
    }

    private void refreshList() {
        WaitPanel.show();
        // mapObjectIdToObjectAggregated.clear();
        clientFactory.getExpensesView().setObjectExpenses(new ArrayList<ObjectExpensesItemDto>());

        ExpenseQBE qbe = new ExpenseQBE(getFirstIndex(place.getPage()), ITEMS_PER_PAGE);
        qbe.setYear(place.getYear());
        clientFactory.getService().getObjectExpenses(qbe, new SimpleAsyncCallback<List<ObjectExpensesItemDto>>() {
            public void onSuccess(List<ObjectExpensesItemDto> result) {
                for (ObjectExpensesItemDto objectExpensesItemDto : result) {
                    mapObjectIdToObjectAggregated.put(objectExpensesItemDto.getLandObject().getId(), objectExpensesItemDto);
                }
                clientFactory.getExpensesView().setObjectExpenses(result);
                WaitPanel.hide();
            }
        });

    }

    public void onObjectClick(final ObjectDto landObject) {
        if (clientFactory.getExpensesView().isObjectOpened(landObject.getId())) {
            clientFactory.getExpensesView().setDataForObject2(landObject, mapObjectIdToObjectAggregated.get(landObject.getId()).getExpences());
        }
        else {
            clientFactory.getExpensesView().setExpensesForObject(landObject, mapObjectIdToObjectAggregated.get(landObject.getId()).getExpences());
        }
        LandManager.setLeft(".window-scroll-aware", Window.getScrollLeft());
    }

    public void onAddExpenseClick(ObjectExpensesItemDto item) {
        ExpenseDto expense = new ExpenseDto();
        expense.setObjectId(item.getLandObject().getId());
        expense.setSumToPay(0d);
        expense.setSumPayed(0d);
        expense.setDate(new Date());

        showEditPopup(expense, item.getLandObject().getAddress());
    }

    public void onExpenseClick(ObjectDto landObject, ExpenseDto expense) {
        showEditPopup(expense, landObject.getAddress());
    }

    private void showEditPopup(ExpenseDto expense, String address) {
        final ExpenseEditPanel panel = ExpenseEditPanel.showInPopup(expense, address, new IPopupHandler<ExpenseDto>() {
            public void onSave(ModalPanel mp, ExpenseDto item) {
                onExpenseSave(item);
            }

            public void onDelete(ModalPanel mp, ExpenseDto item) {
                onExpenseDelete(item);
            }

            public void onCancel(ModalPanel mp) {}
        });
        clientFactory.getService().selectAllExpenseServices(new SimpleAsyncCallback<List<String>>() {
            public void onSuccess(List<String> result) {
                panel.setAllServices(result);
            }
        });
    }

    public void onExpenseSave(final ExpenseDto item) {
        WaitPanel.show();
        clientFactory.getService().saveExpense(item, new SimpleAsyncCallback<Long>() {
            public void onSuccess(Long id) {
                refreshYears();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void onExpenseDelete(final ExpenseDto item) {
        ConfirmModalPanel mp = new ConfirmModalPanel("Подтверждение", "Удалить расходы за "
                + LandManager.MONTHS[item.getDate().getMonth()].toLowerCase() + " ?", "Удалить");
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.DELETE) {
                    WaitPanel.show();
                    clientFactory.getService().deleteExpense(item.getId(), new SimpleAsyncCallback<Void>() {
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