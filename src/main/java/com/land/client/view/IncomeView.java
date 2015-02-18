package com.land.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.land.shared.dto.AggregatedIncomeDto;
import com.land.shared.dto.IncomeDto;
import com.land.shared.dto.ObjectIncomesItemDto;

public interface IncomeView extends IsWidget {

	void setYearBoundaries(Long minYear, Long maxYear);

	void setCurrentYear(Long year);

	void setPageCount(Long pageCount);

	void setCurrentPage(Long currentPage);

	void setTotalObjectAggredatedList(List<AggregatedIncomeDto> arrayList);

	void setObjectIncomes(List<ObjectIncomesItemDto> aggregatedList);

	void setPresenter(Presenter presenter);

	public interface Presenter {

		void onIncomeClick(ObjectIncomesItemDto item, IncomeDto income);

		void onAddIncomeClick(ObjectIncomesItemDto item);

		void onPagerChange(Long newPage);

		void onYearPagerChange(Long newPage);
	}

}
