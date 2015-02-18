package com.land.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.land.shared.dto.AggregatedTaxDto;
import com.land.shared.dto.ObjectTaxesItemDto;
import com.land.shared.dto.TaxDto;

public interface TaxesView extends IsWidget {

	void setPageCount(Long pageCount);

	void setCurrentPage(Long currentPage);

	void setTotalObjectAggredatedList(List<AggregatedTaxDto> result);

	void setObjectTaxes(List<ObjectTaxesItemDto> listObjectTaxes);

	void setPresenter(Presenter presenter);

	public interface Presenter {

		public void onTaxClick(ObjectTaxesItemDto item, TaxDto tax);

		public void onAddTaxClick(ObjectTaxesItemDto item);

		public void onPagerChange(Long newPage);
	}

}
