package com.land.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
import com.land.shared.exception.AnyServiceException;

@RemoteServiceRelativePath("service.rpc")
public interface GreetingService extends RemoteService {

    public UserDto getCurrentUser() throws AnyServiceException;

    Long saveObject(ObjectDto object) throws AnyServiceException;

    Long getObjectDocumentsCount(ObjectQBE qbe) throws AnyServiceException;

    void deleteFile(Long id) throws AnyServiceException;

    void deleteObject(Long id) throws AnyServiceException;

    void deleteTax(Long id) throws AnyServiceException;

    void deleteIncome(Long id) throws AnyServiceException;

    void deleteExpense(Long id) throws AnyServiceException;

    List<ObjectDocumentsItemDto> getObjectDocuments(ObjectQBE qbe) throws AnyServiceException;

    Long getObjectTaxesCount(ObjectQBE qbe) throws AnyServiceException;

    List<ObjectTaxesItemDto> getObjectTaxes(ObjectQBE qbe) throws AnyServiceException;

    Long saveTax(TaxDto object) throws AnyServiceException;

    ObjectYearsBoundariesDto getObjectIncomeBoundaries(ObjectQBE qbe) throws AnyServiceException;

    List<ObjectIncomesItemDto> getObjectIncomes(ObjectIncomeQBE qbe) throws AnyServiceException;

    Long saveIncome(IncomeDto object) throws AnyServiceException;

    ObjectYearsBoundariesDto getObjectExpenseBoundaries(ObjectQBE qbe) throws AnyServiceException;

    Long saveExpense(ExpenseDto object) throws AnyServiceException;

    List<String> selectAllExpenseServices() throws AnyServiceException;

    List<ObjectExpensesItemDto> getObjectExpenses(ExpenseQBE qbe) throws AnyServiceException;

    List<AggregatedExpensesDto> getTotalObjectAggregatedExpenses(AggregatedExpensesQBE qbe) throws AnyServiceException;

    List<AggregatedIncomeDto> getTotalObjectAggregatedIncomes(AggregatedIncomesQBE qbe) throws AnyServiceException;

    List<AggregatedTaxDto> getTotalObjectAggregatedTaxes() throws AnyServiceException;

    Long saveUserByAdmin(UserDto object) throws AnyServiceException;

    Long saveUser(UserDto object) throws AnyServiceException;

    void deleteUser(Long id) throws AnyServiceException;

    Long getUsersCount(UserQBE qbe) throws AnyServiceException;

    List<UserDto> getUsers(UserQBE qbe) throws AnyServiceException;

}
