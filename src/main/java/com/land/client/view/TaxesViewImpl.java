package com.land.client.view;

import java.util.Date;
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
import com.land.client.ui.FileAnchor;
import com.land.client.ui.Pager;
import com.land.shared.dto.AggregatedTaxDto;
import com.land.shared.dto.ObjectTaxesItemDto;
import com.land.shared.dto.TaxDto;

@SuppressWarnings("deprecation")
public class TaxesViewImpl extends Composite implements TaxesView {

	private TaxesView.Presenter presenter = null;

	private FlowPanel viewPanel = new FlowPanel();
	private SimplePanel tableWrapper = new SimplePanel();
	private Grid table;
	private Pager pager = new Pager(Long.valueOf(0));
	private Element[] totalElements = new Element[1];
	private Integer minYear = new Date().getYear() + 1900;
	private Integer maxYear = new Date().getYear() + 1900;

	public TaxesViewImpl() {
		super();
		viewPanel.setStyleName("TaxesView");
		initWidget(viewPanel);
		viewPanel.add(tableWrapper);
		pager.addStyleName("pull-right");
		viewPanel.add(pager);

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

	private void setYearBoundaries(Integer minYear, Integer maxYear) {
		this.minYear = minYear;
		this.maxYear = maxYear;

		totalElements = new Element[maxYear - minYear + 1 + 1];

		table = new Grid(0, (int) (maxYear - minYear + 1 + 1 + 2));
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

		for (Integer i = minYear; i <= maxYear; i++) {
			Element thDocuments = DOM.createTH();
			thDocuments.setInnerText(i + "");
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

		Element td1 = DOM.createTD();
		td1.setInnerText("Итого");
		trf.appendChild(td1);

		trf.appendChild(DOM.createTD());

		for (Integer i = 0; i < maxYear - minYear + 1 + 1; i++) {
			Element td = DOM.createTD();
			td.setInnerText("");
			trf.appendChild(td);
			totalElements[i] = td;
		}
		trf.getFirstChildElement().setClassName("col1");
	}

	public void setTotalObjectAggredatedList(List<AggregatedTaxDto> aggergatedList) {
		Integer minYear = new Date().getYear() + 1900;
		Integer maxYear = new Date().getYear() + 1900;
		for (AggregatedTaxDto aggregatedTaxDto : aggergatedList) {
			if (minYear > aggregatedTaxDto.getYear())
				minYear = aggregatedTaxDto.getYear();
			if (maxYear < aggregatedTaxDto.getYear())
				maxYear = aggregatedTaxDto.getYear();
		}
		setYearBoundaries(minYear, maxYear);

		for (int i = 0; i < totalElements.length; i++)
			totalElements[i].setInnerHTML(Double.valueOf(0) + "");

		Double sum = 0D;
		for (AggregatedTaxDto aggregatedDto : aggergatedList) {
			totalElements[aggregatedDto.getYear().intValue() - minYear].setInnerHTML(aggregatedDto.getSum() + "");
			sum += aggregatedDto.getSum();
		}
		totalElements[totalElements.length - 1].setInnerHTML(sum + "");
	}

	public void setObjectTaxes(List<ObjectTaxesItemDto> listObjectTaxes) {
		table.clear();
		table.resizeRows(listObjectTaxes.size());

		int i = 0;
		for (final ObjectTaxesItemDto objectTaxesItemDto : listObjectTaxes) {
			table.setText(i, 0, objectTaxesItemDto.getLandObject().getAddress());

			Button addTaxButton = new Button("<i class='icon-plus'></i>");
			addTaxButton.setStyleName("btn btn-mini btn-primary");
			addTaxButton.setTitle("Добавить налог");
			addTaxButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					presenter.onAddTaxClick(objectTaxesItemDto);
				}
			});
			table.setWidget(i, 1, addTaxButton);
			table.getCellFormatter().setStyleName(i, 0, "col1");
			table.getRowFormatter().setStyleName(i, "row-begin");

			Double sumValue = 0D;
			for (final TaxDto tax : objectTaxesItemDto.getTaxes()) {
				FlowPanel taxPanel = new FlowPanel();
				taxPanel.setStyleName("tax");
				table.setWidget(i, (int) (2 + tax.getYear() - minYear), taxPanel);

				Anchor taxAnchor = new Anchor(tax.getValue() + "");
				taxAnchor.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						presenter.onTaxClick(objectTaxesItemDto, tax);
					}
				});
				taxPanel.add(taxAnchor);

				FileAnchor receiptAnchor = new FileAnchor(tax.getReceipt());
				receiptAnchor.setText("");
				taxPanel.add(receiptAnchor);
				receiptAnchor.setVisible(tax.getReceipt() != null);
				sumValue += tax.getValue();
			}
			table.setText(i, 2 + maxYear - minYear + 1, sumValue + "");

			i++;
		}
	}

}
