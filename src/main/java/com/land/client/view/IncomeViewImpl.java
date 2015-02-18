package com.land.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.SimplePanel;
import com.land.client.LandManager;
import com.land.client.ui.Pager;
import com.land.client.ui.form.DivWidget;
import com.land.shared.dto.AggregatedIncomeDto;
import com.land.shared.dto.IncomeDto;
import com.land.shared.dto.ObjectIncomesItemDto;

public class IncomeViewImpl extends Composite implements IncomeView {

	private IncomeView.Presenter presenter = null;

	private FlowPanel viewPanel = new FlowPanel();
	private SimplePanel tableWrapper = new SimplePanel();
	private Grid table = new Grid(0, 2 + LandManager.MONTHS.length + 1);

	private Pager yearPager = new Pager(Long.valueOf(0));
	private Pager pager = new Pager(Long.valueOf(0));

	private Element[] totalElements = new Element[LandManager.MONTHS.length + 1];

	public IncomeViewImpl() {
		super();
		viewPanel.setStyleName("IncomeView");
		initWidget(viewPanel);
		viewPanel.add(yearPager);

		yearPager.addStyleName("pagination-centered pagination-large");

		viewPanel.add(tableWrapper);
		pager.addStyleName("pull-right");
		viewPanel.add(pager);

		table.setStyleName("table table-striped table-bordered");
		tableWrapper.setWidget(table);

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

		for (int i = 0; i < LandManager.MONTHS.length; i++) {
			Element thDocuments = DOM.createTH();
			thDocuments.setInnerText(LandManager.MONTHS[i]);
			tr.appendChild(thDocuments);
		}

		Element thTotal = DOM.createTH();
		thTotal.setInnerText("Итого");
		tr.appendChild(thTotal);
		tr.getFirstChildElement().setClassName("col1");

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

	public void setObjectIncomes(List<ObjectIncomesItemDto> objectIncomes) {
		table.clear();
		table.resizeRows(objectIncomes.size());

		int i = 0;
		for (final ObjectIncomesItemDto objectAggregatedItem : objectIncomes) {
			DivWidget addressAnchorHidden = new DivWidget(objectAggregatedItem.getLandObject().getAddress());
			addressAnchorHidden.setStyleName("anchor-hidden");

			DivWidget addressAnchor = new DivWidget(objectAggregatedItem.getLandObject().getAddress());
			addressAnchor.setStyleName("anchor window-scroll-aware");

			FlowPanel div = new FlowPanel();
			div.add(addressAnchorHidden);
			div.add(addressAnchor);

			table.setWidget(i, 0, div);
			table.getCellFormatter().setStyleName(i, 0, "col1");
			table.getRowFormatter().setStyleName(i, "row-begin");

			Button addTaxButton = new Button("<i class='icon-plus'></i>");
			addTaxButton.setStyleName("btn btn-mini btn-primary");
			addTaxButton.setTitle("Добавить налог");
			addTaxButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					presenter.onAddIncomeClick(objectAggregatedItem);
				}
			});
			table.setWidget(i, 1, addTaxButton);

			Double sumValue = 0D;
			for (final IncomeDto income : objectAggregatedItem.getIncomes()) {
				Anchor incomeAnchor = new Anchor(income.getValue() + "");
				incomeAnchor.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						presenter.onIncomeClick(objectAggregatedItem, income);
					}
				});
				table.setWidget(i, (int) (2 + income.getMonth()), incomeAnchor);
				sumValue += income.getValue();
			}
			table.setText(i, 2 + LandManager.MONTHS.length, sumValue + "");
			i++;
		}
	}

	public void setYearBoundaries(Long minYear, Long maxYear) {
		yearPager.setFirstAndLastPage(minYear, maxYear);

	}

	public void setCurrentYear(Long year) {
		yearPager.setCurrentPage(year);
	}

	public void setTotalObjectAggredatedList(List<AggregatedIncomeDto> result) {
		for (int i = 0; i < totalElements.length; i++)
			totalElements[i].setInnerHTML(Double.valueOf(0) + "");

		Double sum = 0D;
		for (AggregatedIncomeDto aggregatedDto : result) {
			totalElements[aggregatedDto.getMonth().intValue()].setInnerHTML(aggregatedDto.getSum() + "");
			sum += aggregatedDto.getSum();
		}
		totalElements[totalElements.length - 1].setInnerHTML(sum + "");
	}
}
