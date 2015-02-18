package com.land.client.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.land.client.LandManager;
import com.land.client.ui.FileAnchor;
import com.land.client.ui.Pager;
import com.land.client.ui.form.DivWidget;
import com.land.shared.dto.AggregatedExpensesDto;
import com.land.shared.dto.ExpenseDto;
import com.land.shared.dto.ObjectDto;
import com.land.shared.dto.ObjectExpensesItemDto;

@SuppressWarnings("deprecation")
public class ExpensesViewImpl extends Composite implements ExpensesView {

	private static final String ROW_BEGIN = "row-begin";

	private ExpensesView.Presenter presenter = null;

	private FlowPanel viewPanel = new FlowPanel();
	private SimplePanel tableWrapper = new SimplePanel();
	private FlexTable table = new FlexTable();

	private Pager yearPager = new Pager(Long.valueOf(0));
	private Pager pager = new Pager(Long.valueOf(0));

	private Element[] totalElements = new Element[LandManager.MONTHS.length * 2 + 1];
	private Map<Long, Integer> mapObjectIdToRow = new LinkedHashMap<Long, Integer>();

	public ExpensesViewImpl() {
		super();
		viewPanel.setStyleName("ExpensesView");
		initWidget(viewPanel);
		viewPanel.add(yearPager);

		yearPager.addStyleName("pagination-centered pagination-large");

		viewPanel.add(tableWrapper);
		pager.addStyleName("pull-right");
		viewPanel.add(pager);

		table.setStyleName("table table-striped table-bordered");
		tableWrapper.setWidget(table);

		// header
		Element thead = DOM.createTHead();
		table.getElement().insertAfter(thead, table.getElement().getFirstChild());

		Element tr = DOM.createTR();
		thead.appendChild(tr);

		Element thObject = DOM.createTH();
		thObject.setInnerText("Объект");
		tr.appendChild(thObject);

		Element thAddTax = DOM.createTH();
		thAddTax.addClassName("addTax");
		thAddTax.setInnerText("");
		tr.appendChild(thAddTax);

		Element thService = DOM.createTH();
		thService.addClassName("service");
		thService.setInnerText("");
		tr.appendChild(thService);

		for (int i = 0; i < LandManager.MONTHS.length; i++) {
			Element th = DOM.createTH();
			th.setInnerText(LandManager.MONTHS[i]);
			tr.appendChild(th);
			th.setAttribute("colspan", "2");
		}

		Element thTotal = DOM.createTH();
		thTotal.setInnerText("Итого");
		tr.appendChild(thTotal);
		tr.getFirstChildElement().setClassName("col1");

		Element tr2 = DOM.createTR();
		tr2.addClassName("level2");
		thead.appendChild(tr2);
		tr2.appendChild(DOM.createTH());
		tr2.appendChild(DOM.createTH());
		tr2.appendChild(DOM.createTH());
		for (int i = 0; i < LandManager.MONTHS.length * 2; i++) {
			Element th = DOM.createTH();
			th.setInnerText(i % 2 == 0 ? "Заплатили" : "Долг");
			tr2.appendChild(th);
		}
		tr2.appendChild(DOM.createTH());
		tr2.getFirstChildElement().setClassName("col1");

		// footer
		Element tFoot = DOM.createTFoot();
		table.getElement().insertAfter(tFoot, table.getElement().getLastChild());

		Element trf = DOM.createTR();
		tFoot.appendChild(trf);

		Element thTotalDivLabelHidden = DOM.createDiv();
		thTotalDivLabelHidden.setClassName("anchor-hidden");
		thTotalDivLabelHidden.setInnerText("Итого");
		Element thTotalDivLabel = DOM.createDiv();
		thTotalDivLabel.setClassName("anchor window-scroll-aware");
		thTotalDivLabel.setInnerText("Итого");
		Element thTotalDiv = DOM.createDiv();
		Element td1 = DOM.createTD();
		td1.appendChild(thTotalDiv);
		thTotalDiv.appendChild(thTotalDivLabelHidden);
		thTotalDiv.appendChild(thTotalDivLabel);
		trf.appendChild(td1);

		trf.appendChild(DOM.createTD());
		trf.appendChild(DOM.createTD());

		for (int i = 0; i < totalElements.length; i++) {
			Element td = DOM.createTD();
			td.setInnerText("");
			trf.appendChild(td);
			totalElements[i] = td;
		}
		trf.getFirstChildElement().setClassName("col1");

		yearPager.setChangeHandler(new Pager.IChangeHandler() {
			public void onChange(Long newPage) {
				presenter.onYearPagerChange(newPage);

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

	public void setObjectExpenses(List<ObjectExpensesItemDto> objectExpenses) {
		table.clear();
		while (table.getRowCount() > 0)
			table.removeRow(0);
		mapObjectIdToRow.clear();

		int i = 0;
		for (final ObjectExpensesItemDto objectExpense : objectExpenses) {
			Anchor addressAnchorHidden = new Anchor(objectExpense.getLandObject().getAddress());
			addressAnchorHidden.setStyleName("anchor-hidden");

			Anchor addressAnchor = new Anchor(objectExpense.getLandObject().getAddress());
			addressAnchor.setStyleName("anchor window-scroll-aware");

			FlowPanel div = new FlowPanel();
			div.add(addressAnchorHidden);
			div.add(addressAnchor);

			table.setWidget(i, 0, div);
			table.getCellFormatter().setStyleName(i, 0, "col1");
			addressAnchorHidden.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					presenter.onObjectClick(objectExpense.getLandObject());
				}
			});
			addressAnchor.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					presenter.onObjectClick(objectExpense.getLandObject());
				}
			});

			Button addTaxButton = new Button("<i class='icon-plus'></i>");
			addTaxButton.setStyleName("btn btn-mini btn-primary");
			addTaxButton.setTitle("Добавить счёт");
			addTaxButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					presenter.onAddExpenseClick(objectExpense);
				}
			});
			table.setWidget(i, 1, addTaxButton);
			mapObjectIdToRow.put(objectExpense.getLandObject().getId(), i);

			i++;
		}
		for (final ObjectExpensesItemDto objectExpense : objectExpenses) {
			setExpensesForObject(objectExpense.getLandObject(), objectExpense.getExpences());
		}
	}

	private void updateRowSpan(Long objectId, int newRowSpan) {
		int row = mapObjectIdToRow.get(objectId);
		int oldRowSpan = table.getFlexCellFormatter().getRowSpan(row, 0);
		if (oldRowSpan > newRowSpan) {
			for (int k = newRowSpan; k < oldRowSpan; k++)
				table.removeRow(row + 1);
			boolean after = false;
			for (Entry<Long, Integer> entry : mapObjectIdToRow.entrySet()) {
				if (after)
					entry.setValue(entry.getValue() - (oldRowSpan - newRowSpan));
				if (entry.getKey().equals(objectId))
					after = true;
			}
		} else {
			for (int k = oldRowSpan; k < newRowSpan; k++) {
				table.insertRow(row + 1);
			}
			boolean after = false;
			for (Entry<Long, Integer> entry : mapObjectIdToRow.entrySet()) {
				if (after) {
					entry.setValue(entry.getValue() + (newRowSpan - oldRowSpan));
				}
				if (entry.getKey().equals(objectId))
					after = true;
			}
		}

		for (int i = row; i < row + newRowSpan; i++) {
			table.removeCells(i, 2, table.getCellCount(i) - 2);
		}

		table.getFlexCellFormatter().setRowSpan(row, 0, newRowSpan);
		table.getFlexCellFormatter().setRowSpan(row, 1, newRowSpan);
		table.getRowFormatter().setStyleName(row, ROW_BEGIN);
	}

	public void setDataForObject2(ObjectDto landObject, List<ExpenseDto> expences) {
		Map<Integer, AggregatedExpensesDto> mapMonthToExpenses = new LinkedHashMap<Integer, AggregatedExpensesDto>();
		for (ExpenseDto expenseDto : expences) {
			AggregatedExpensesDto aggregatedExpense = mapMonthToExpenses.get(expenseDto.getDate().getMonth());
			if (aggregatedExpense == null) {
				aggregatedExpense = new AggregatedExpensesDto();
				aggregatedExpense.setSumPayed(0d);
				aggregatedExpense.setSumToPay(0d);
				aggregatedExpense.setMonth(Long.valueOf(expenseDto.getDate().getMonth()));
				aggregatedExpense.setYear(Long.valueOf(expenseDto.getDate().getYear()) + 1900);
				aggregatedExpense.setObjectId(landObject.getId());
				mapMonthToExpenses.put(expenseDto.getDate().getMonth(), aggregatedExpense);
			}

			aggregatedExpense.setSumPayed(aggregatedExpense.getSumPayed() + expenseDto.getSumPayed());
			aggregatedExpense.setSumToPay(aggregatedExpense.getSumToPay() + expenseDto.getSumToPay());
		}
		setDataForObject(landObject, new ArrayList<AggregatedExpensesDto>(mapMonthToExpenses.values()));
	}

	public void setDataForObject(ObjectDto landObject, List<AggregatedExpensesDto> aggregatedData) {
		Double sum = 0D;
		int row = mapObjectIdToRow.get(landObject.getId());
		updateRowSpan(landObject.getId(), 1);

		table.setText(row, 2, "");

		for (int col = 3; col < 3 + LandManager.MONTHS.length * 2; col++) {
			table.setText(row, col, "");
		}

		for (AggregatedExpensesDto aggregatedDto : aggregatedData) {
			sum += aggregatedDto.getSumPayed();
			table.setText(row, (int) (3 + 2 * aggregatedDto.getMonth()), aggregatedDto.getSumPayed() + "");
			table.setText(row, (int) (3 + 2 * aggregatedDto.getMonth() + 1), aggregatedDto.countDebt() + "");
		}
		table.setText(row, (int) (3 + LandManager.MONTHS.length * 2), sum + "");
	}

	public void setExpensesForObject(final ObjectDto landObject, List<ExpenseDto> expenses) {
		int row = mapObjectIdToRow.get(landObject.getId());

		Map<String, List<ExpenseDto>> mapServiceNameToExpenses = new LinkedHashMap<String, List<ExpenseDto>>();
		for (ExpenseDto expenseDto : expenses) {
			List<ExpenseDto> list = mapServiceNameToExpenses.get(expenseDto.getService());
			if (list == null) {
				list = new ArrayList<ExpenseDto>();
				mapServiceNameToExpenses.put(expenseDto.getService(), list);
			}
			list.add(expenseDto);
		}

		int newRowSpan = Math.max(mapServiceNameToExpenses.size(), 1);
		updateRowSpan(landObject.getId(), newRowSpan);

		int i = row;
		// пустая строка если добавлять некого
		int startCol = 3;

		if (mapServiceNameToExpenses.entrySet().isEmpty()) {
			for (int col = startCol; col < startCol + LandManager.MONTHS.length * 2; col++) {
				table.setText(i, col, "");
			}
			table.setText(i, startCol + LandManager.MONTHS.length * 2, "");
			i++;
		}

		//expenses
		for (Entry<String, List<ExpenseDto>> entry : mapServiceNameToExpenses.entrySet()) {
			FlowPanel div = new FlowPanel();
			div.add(new DivWidget(entry.getKey(), "content-hidden"));
			div.add(new DivWidget(entry.getKey(), "content window-scroll-aware"));
			table.setWidget(i, startCol - 1, div);
			table.getCellFormatter().setStyleName(i, startCol - 1, "col-service");

			for (int col = startCol; col < startCol + LandManager.MONTHS.length * 2; col++) {
				table.setText(i, col, "");
			}

			Double sumPayed = 0D;
			for (final ExpenseDto expense : entry.getValue()) {
				sumPayed += expense.getSumPayed();

				FlowPanel fp = new FlowPanel();
				fp.setStyleName("expenses");

				Widget w = table.getWidget(i, startCol + expense.getDate().getMonth() * 2);
				if (w instanceof FlowPanel)
					fp = (FlowPanel) w;
				else
					table.setWidget(i, startCol + expense.getDate().getMonth() * 2, fp);

				FlowPanel expensePanel = new FlowPanel();
				expensePanel.setStyleName("expense");

				Anchor arrgegatedAnchor = new Anchor(expense.getSumPayed() + "");
				arrgegatedAnchor.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						presenter.onExpenseClick(landObject, expense);
					}
				});
				expensePanel.add(arrgegatedAnchor);

				if (expense.getReceipt() != null) {
					FileAnchor fileAnchor = new FileAnchor(expense.getReceipt());
					fileAnchor.setText("");
					expensePanel.add(fileAnchor);
				}
				fp.add(expensePanel);

				///
				FlowPanel expenseDebtsPanel = new FlowPanel();
				expenseDebtsPanel.setStyleName("expenseDebts");
				Widget wDebt = table.getWidget(i, startCol + 1 + expense.getDate().getMonth() * 2);
				if (wDebt instanceof FlowPanel)
					expenseDebtsPanel = (FlowPanel) wDebt;
				else
					table.setWidget(i, startCol + 1 + expense.getDate().getMonth() * 2, expenseDebtsPanel);

				FlowPanel expenseDebtPanel = new FlowPanel();
				expenseDebtPanel.setStyleName("expenseDebt");
				Anchor arrgegatedDebtAnchor = new Anchor(expense.countDebt() + "");
				arrgegatedDebtAnchor.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						presenter.onExpenseClick(landObject, expense);
					}
				});
				expenseDebtPanel.add(arrgegatedDebtAnchor);
				expenseDebtsPanel.add(expenseDebtPanel);
			}
			table.setText(i, startCol + LandManager.MONTHS.length * 2, sumPayed + "");

			i++;
			startCol = 1;
		}
	}

	public void setYearBoundaries(Long minYear, Long maxYear) {
		yearPager.setFirstAndLastPage(minYear, maxYear);

	}

	public void setCurrentYear(Long year) {
		yearPager.setCurrentPage(year);
	}

	public void setTotalObjectAggredatedList(List<AggregatedExpensesDto> result) {
		for (int i = 0; i < totalElements.length; i++)
			totalElements[i].setInnerHTML(Double.valueOf(0) + "");

		Double sum = 0D;
		for (AggregatedExpensesDto aggregatedDto : result) {
			totalElements[aggregatedDto.getMonth().intValue() * 2].setInnerHTML(aggregatedDto.getSumPayed() + "");
			totalElements[aggregatedDto.getMonth().intValue() * 2 + 1].setInnerHTML(aggregatedDto.countDebt() + "");
			sum += aggregatedDto.getSumPayed();
		}
		totalElements[totalElements.length - 1].setInnerHTML(sum + "");
	}

	public boolean isObjectOpened(Long id) {
		int row = mapObjectIdToRow.get(id);
		int oldRowSpan = table.getFlexCellFormatter().getRowSpan(row, 0);
		return oldRowSpan != 1;
	}

}
