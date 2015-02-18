package com.land.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.land.shared.dto.AggregatedExpensesDto;
import com.land.shared.dto.ExpenseDto;
import com.land.shared.dto.ObjectDto;
import com.land.shared.dto.ObjectExpensesItemDto;

public interface ExpensesView extends IsWidget {

	void setYearBoundaries(Long minYear, Long maxYear);

	void setCurrentYear(Long year);

	void setPageCount(Long pageCount);

	void setCurrentPage(Long currentPage);

	void setDataForObject(ObjectDto landObject, List<AggregatedExpensesDto> aggregatedData);

	void setDataForObject2(ObjectDto landObject, List<ExpenseDto> expences);

	void setExpensesForObject(ObjectDto landObject, List<ExpenseDto> result);

	void setTotalObjectAggredatedList(List<AggregatedExpensesDto> result);

	void setObjectExpenses(List<ObjectExpensesItemDto> result);

	boolean isObjectOpened(Long id);

	void setPresenter(Presenter presenter);

	public interface Presenter {

		void onExpenseClick(ObjectDto landObject, ExpenseDto expene);

		void onAddExpenseClick(ObjectExpensesItemDto item);

		void onPagerChange(Long newPage);

		void onYearPagerChange(Long newPage);

		void onObjectClick(ObjectDto landObject);
	}

}
