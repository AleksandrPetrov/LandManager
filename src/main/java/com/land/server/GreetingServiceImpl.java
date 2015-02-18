package com.land.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.land.client.GreetingService;
import com.land.server.dao.ExpenseDao;
import com.land.server.dao.ExpenseServiceDao;
import com.land.server.dao.FileDao;
import com.land.server.dao.IncomeDao;
import com.land.server.dao.ObjectDao;
import com.land.server.dao.TaxDao;
import com.land.server.dao.UserDao;
import com.land.server.domain.Expense;
import com.land.server.domain.File;
import com.land.server.domain.Income;
import com.land.server.domain.LandObject;
import com.land.server.domain.Tax;
import com.land.server.domain.User;
import com.land.server.security.CurrentUser;
import com.land.shared.dto.AggregatedExpensesDto;
import com.land.shared.dto.AggregatedIncomeDto;
import com.land.shared.dto.AggregatedTaxDto;
import com.land.shared.dto.EntityDto;
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
import com.land.shared.dto.qbe.FileQBE;
import com.land.shared.dto.qbe.ObjectIncomeQBE;
import com.land.shared.dto.qbe.ObjectQBE;
import com.land.shared.dto.qbe.UserQBE;
import com.land.shared.exception.AnyServiceException;

@Transactional
@Service
public class GreetingServiceImpl implements GreetingService {

    @Autowired
    ExpenseDao expenseDao;
    @Autowired
    ExpenseServiceDao expenseServiceDao;
    @Autowired
    IncomeDao incomeDao;
    @Autowired
    ObjectDao objectDao;
    @Autowired
    TaxDao taxDao;
    @Autowired
    FileDao fileDao;
    @Autowired
    UserDao userDao;

    public UserDto getCurrentUser() throws AnyServiceException {
        User user = userDao.findById(CurrentUser.getId());
        return user == null ? null : user.toDto();
    }

    @Secured({CurrentUser.ROLE_USER})
    public List<Long> buildIdList(List<? extends EntityDto> list) {
        List<Long> res = new ArrayList<Long>();
        for (EntityDto entityDto : list)
            res.add(entityDto.getId());
        return res;
    }

    @Secured({CurrentUser.ROLE_USER})
    public Long getObjectDocumentsCount(ObjectQBE qbe) {
        return objectDao.getObjectsCount(qbe);
    }

    @Secured({CurrentUser.ROLE_USER})
    public List<ObjectDocumentsItemDto> getObjectDocuments(ObjectQBE qbe) throws AnyServiceException {
        List<ObjectDocumentsItemDto> res = new ArrayList<ObjectDocumentsItemDto>();
        List<LandObject> list = objectDao.selectObjects(qbe);
        for (LandObject landObject : list) {
            ObjectDocumentsItemDto resItem = new ObjectDocumentsItemDto();
            resItem.setId(landObject.getId());
            resItem.setAddress(landObject.getAddress());
            resItem.setTaxesView(landObject.getTaxesView());
            resItem.setExpencesView(landObject.getExpencesView());
            resItem.setIncomesView(landObject.getIncomesView());
            res.add(resItem);

            FileQBE fileQbe = new FileQBE();
            fileQbe.setObjectId(landObject.getId());
            List<File> files = fileDao.selectFiles(fileQbe);
            for (File file : files) {
                resItem.getDocuments().add(file.toDto());
            }
        }

        return res;
    }

    @Secured({CurrentUser.ROLE_USER})
    public Long saveObject(ObjectDto object) throws AnyServiceException {
        Long res = null;
        LandObject landObject = objectDao.save(object);
        res = landObject.getId();
        return res;
    }

    @Secured({CurrentUser.ROLE_USER})
    public TaxDto getTax(Long id) throws AnyServiceException {
        TaxDto res = null;
        Tax tax = taxDao.findById(id);
        if (tax != null) res = tax.toDto();
        return res;
    }

    @Secured({CurrentUser.ROLE_USER})
    public Long saveTax(TaxDto object) throws AnyServiceException {
        Long res = null;
        Tax tax = taxDao.save(object);
        res = tax.getId();
        return res;
    }

    @Secured({CurrentUser.ROLE_USER})
    public Long getObjectTaxesCount(ObjectQBE qbe) throws AnyServiceException {
        return objectDao.getObjectsCount(qbe);
    }

    @Secured({CurrentUser.ROLE_USER})
    public List<ObjectTaxesItemDto> getObjectTaxes(ObjectQBE qbe) throws AnyServiceException {
        List<ObjectTaxesItemDto> res = new ArrayList<ObjectTaxesItemDto>();
        List<LandObject> list = objectDao.selectObjects(qbe);
        for (LandObject landObject : list) {
            ObjectTaxesItemDto resItem = new ObjectTaxesItemDto();
            resItem.setLandObject(landObject.toDto());
            res.add(resItem);

            for (Tax tax : landObject.getTaxes()) {
                TaxDto resItemTax = tax.toDto();
                resItem.getTaxes().add(resItemTax);
            }
        }
        return res;
    }

    @Secured({CurrentUser.ROLE_USER})
    public Long saveIncome(IncomeDto object) throws AnyServiceException {
        Long res = null;
        Income tax = incomeDao.save(object);
        res = tax.getId();
        return res;
    }

    @Secured({CurrentUser.ROLE_USER})
    public ObjectYearsBoundariesDto getObjectIncomeBoundaries(ObjectQBE qbe) throws AnyServiceException {
        return incomeDao.getObjectIncomeBoundaries(qbe);
    }

    @Secured({CurrentUser.ROLE_USER})
    public List<ObjectIncomesItemDto> getObjectIncomes(ObjectIncomeQBE qbe) throws AnyServiceException {
        Map<Long, ObjectIncomesItemDto> resMap = new LinkedHashMap<Long, ObjectIncomesItemDto>();
        ObjectQBE objectQbe = new ObjectQBE(qbe.getFirst(), qbe.getCount());
        objectQbe.setIncomesView(true);
        List<ObjectDto> list = objectDao.selectObjectsDto(objectQbe);
        for (ObjectDto objectDto : list) {
            ObjectIncomesItemDto resItem = new ObjectIncomesItemDto();
            resItem.setLandObject(objectDto);
            resMap.put(objectDto.getId(), resItem);
        }

        List<Long> idList = buildIdList(list);
        qbe.setObjectIdList(idList);
        qbe.setFirst(0);
        qbe.setCount(-1);

        List<IncomeDto> listAggregated = incomeDao.selectIncomes(qbe);
        for (IncomeDto aggregatedDto : listAggregated)
            resMap.get(aggregatedDto.getObjectId()).getIncomes().add(aggregatedDto);

        return new ArrayList<ObjectIncomesItemDto>(resMap.values());
    }

    @Secured({CurrentUser.ROLE_USER})
    public Long saveExpense(ExpenseDto object) throws AnyServiceException {
        Long res = null;
        Expense income = expenseDao.save(object);
        res = income.getId();
        return res;
    }

    @Secured({CurrentUser.ROLE_USER})
    public ObjectYearsBoundariesDto getObjectExpenseBoundaries(ObjectQBE qbe) throws AnyServiceException {
        return expenseDao.getObjectExpenseBoundaries(qbe);
    }

    public List<AggregatedExpensesDto> getTotalObjectAggregatedExpenses(AggregatedExpensesQBE qbe)
            throws AnyServiceException {
        return expenseDao.selectTotalAggregatedExpenses(qbe, null);
    }

    @Secured({CurrentUser.ROLE_USER})
    public List<ObjectExpensesItemDto> getObjectExpenses(ExpenseQBE qbe) throws AnyServiceException {
        Map<Long, ObjectExpensesItemDto> resMap = new LinkedHashMap<Long, ObjectExpensesItemDto>();
        ObjectQBE objectQbe = new ObjectQBE(qbe.getFirst(), qbe.getCount());
        objectQbe.setExpencesView(true);
        List<ObjectDto> list = objectDao.selectObjectsDto(objectQbe);
        for (ObjectDto objectDto : list) {
            ObjectExpensesItemDto resItem = new ObjectExpensesItemDto();
            resItem.setLandObject(objectDto);
            resMap.put(objectDto.getId(), resItem);
        }

        List<Long> idList = buildIdList(list);
        qbe.setObjectIdList(idList);
        qbe.setFirst(0);
        qbe.setCount(-1);

        List<ExpenseDto> listExpenses = expenseDao.selectExpenses(qbe);
        for (ExpenseDto expense : listExpenses)
            resMap.get(expense.getObjectId()).getExpences().add(expense);

        return new ArrayList<ObjectExpensesItemDto>(resMap.values());
    }

    @Secured({CurrentUser.ROLE_USER})
    public List<String> selectAllExpenseServices() throws AnyServiceException {
        return expenseServiceDao.selectAllNames();
    }

    @Secured({CurrentUser.ROLE_USER})
    public void deleteObject(Long id) throws AnyServiceException {
        objectDao.remove(id);
    }

    @Secured({CurrentUser.ROLE_USER})
    public void deleteTax(Long id) throws AnyServiceException {
        taxDao.remove(id);
    }

    @Secured({CurrentUser.ROLE_USER})
    public void deleteIncome(Long id) throws AnyServiceException {
        incomeDao.remove(id);
    }

    @Secured({CurrentUser.ROLE_USER})
    public void deleteExpense(Long id) throws AnyServiceException {
        expenseDao.remove(id);
    }

    @Secured({CurrentUser.ROLE_USER})
    public void deleteFile(Long id) throws AnyServiceException {
        fileDao.remove(id);
    }

    @Secured({CurrentUser.ROLE_USER})
    public List<AggregatedIncomeDto> getTotalObjectAggregatedIncomes(AggregatedIncomesQBE qbe)
            throws AnyServiceException {
        return incomeDao.selectTotalAggregatedIncomes(qbe, null);
    }

    @Secured({CurrentUser.ROLE_USER})
    public List<AggregatedTaxDto> getTotalObjectAggregatedTaxes() throws AnyServiceException {
        return taxDao.selectTotalAggregatedTaxes(null);
    }

    @Secured({CurrentUser.ROLE_USER})
    public Long saveUser(UserDto object) throws AnyServiceException {
        return userDao.save(object, true).getId();
    }

    @Secured({CurrentUser.ROLE_ADMIN})
    public Long saveUserByAdmin(UserDto object) throws AnyServiceException {
        return userDao.save(object, false).getId();
    }

    @Secured({CurrentUser.ROLE_ADMIN})
    public void deleteUser(Long id) throws AnyServiceException {
        userDao.remove(id);
    }

    @Secured({CurrentUser.ROLE_ADMIN})
    public Long getUsersCount(UserQBE qbe) throws AnyServiceException {
        return userDao.getUsersCount(qbe);
    }

    @Secured({CurrentUser.ROLE_ADMIN})
    public List<UserDto> getUsers(UserQBE qbe) throws AnyServiceException {
        return userDao.getUsersDto(qbe);
    }
}
