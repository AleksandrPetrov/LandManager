package com.land.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.land.shared.dto.AggregatedExpensesDto;
import com.land.shared.dto.AggregatedIncomeDto;
import com.land.shared.dto.AggregatedTaxDto;
import com.land.shared.dto.ExpenseDto;
import com.land.shared.dto.IncomeDto;
import com.land.shared.dto.ObjectDocumentsItemDto;
import com.land.shared.dto.ObjectDto;
import com.land.shared.dto.ObjectExpensesItemDto;
import com.land.shared.dto.ObjectIncomesItemDto;
import com.land.shared.dto.ObjectTaxesItemDto;
import com.land.shared.dto.ObjectYearsBoundariesDto;
import com.land.shared.dto.TaxDto;
import com.land.shared.dto.UserDto;
import com.land.shared.dto.qbe.AggregatedExpensesQBE;
import com.land.shared.dto.qbe.AggregatedIncomesQBE;
import com.land.shared.dto.qbe.ExpenseQBE;
import com.land.shared.dto.qbe.ObjectIncomeQBE;
import com.land.shared.dto.qbe.ObjectQBE;
import com.land.shared.dto.qbe.UserQBE;

public interface GreetingServiceAsync {

    // object
    void getObjectDocuments(ObjectQBE qbe, AsyncCallback<List<ObjectDocumentsItemDto>> callback);

    void getObjectDocumentsCount(ObjectQBE qbe, AsyncCallback<Long> callback);

    void saveObject(ObjectDto object, AsyncCallback<Long> callback);

    void deleteObject(Long id, AsyncCallback<Void> callback);

    // income
    void getTotalObjectAggregatedIncomes(AggregatedIncomesQBE qbe, AsyncCallback<List<AggregatedIncomeDto>> callback);

    void getObjectIncomeBoundaries(ObjectQBE qbe, AsyncCallback<ObjectYearsBoundariesDto> callback);

    void getObjectIncomes(ObjectIncomeQBE qbe, AsyncCallback<List<ObjectIncomesItemDto>> callback);

    void saveIncome(IncomeDto object, AsyncCallback<Long> callback);

    void deleteIncome(Long id, AsyncCallback<Void> callback);

    // expense
    void getTotalObjectAggregatedExpenses(AggregatedExpensesQBE qbe, AsyncCallback<List<AggregatedExpensesDto>> callback);

    void getObjectExpenseBoundaries(ObjectQBE qbe, AsyncCallback<ObjectYearsBoundariesDto> callback);

    void getObjectExpenses(ExpenseQBE qbe, AsyncCallback<List<ObjectExpensesItemDto>> callback);

    void saveExpense(ExpenseDto object, AsyncCallback<Long> callback);

    void deleteExpense(Long id, AsyncCallback<Void> callback);

    void selectAllExpenseServices(AsyncCallback<List<String>> callback);

    // tax
    void getTotalObjectAggregatedTaxes(AsyncCallback<List<AggregatedTaxDto>> callback);

    void getObjectTaxes(ObjectQBE qbe, AsyncCallback<List<ObjectTaxesItemDto>> callback);

    void getObjectTaxesCount(ObjectQBE qbe, AsyncCallback<Long> callback);

    void deleteTax(Long id, AsyncCallback<Void> callback);

    void saveTax(TaxDto object, AsyncCallback<Long> callback);

    // file
    void deleteFile(Long id, AsyncCallback<Void> callback);

    void getCurrentUser(AsyncCallback<UserDto> callback);

    // user
    void getUsersCount(UserQBE qbe, AsyncCallback<Long> callback);

    void getUsers(UserQBE qbe, AsyncCallback<List<UserDto>> callback);

    void deleteUser(Long id, AsyncCallback<Void> callback);

    void saveUser(UserDto object, AsyncCallback<Long> callback);

    void saveUserByAdmin(UserDto object, AsyncCallback<Long> callback);

}
