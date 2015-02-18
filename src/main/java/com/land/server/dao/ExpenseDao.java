package com.land.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.land.server.domain.Expense;
import com.land.server.domain.ExpenseService;
import com.land.server.domain.File;
import com.land.server.domain.LandObject;
import com.land.shared.dto.AggregatedExpensesDto;
import com.land.shared.dto.ExpenseDto;
import com.land.shared.dto.ObjectYearsBoundariesDto;
import com.land.shared.dto.qbe.AggregatedExpensesQBE;
import com.land.shared.dto.qbe.ExpenseQBE;
import com.land.shared.dto.qbe.ObjectQBE;
import com.land.shared.exception.AnyServiceException;

@SuppressWarnings({"unchecked", "deprecation"})
@Repository
public class ExpenseDao extends JpaDao<Expense> {

    @Autowired
    ObjectDao objectDao;

    @Autowired
    ExpenseServiceDao expenseServiceDao;

    @Override
    public Class<Expense> getEntityClass() {
        return Expense.class;
    }

    public List<AggregatedExpensesDto> selectTotalAggregatedExpenses(AggregatedExpensesQBE qbe, List<Long> objectIdList) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        if (qbe.getYear() != null) {
            criteria.add(Restrictions.sqlRestriction("YEAR(this_.c_date) = ?", qbe.getYear(), Hibernate.LONG));
        }
        if (objectIdList != null) {
            if (objectIdList.isEmpty()) {
                criteria.add(Restrictions.isNull("id"));
            }
            else
                criteria.add(Restrictions.in("object.id", objectIdList));
        }
        criteria.setProjection(Projections.projectionList().add(Projections.sqlGroupProjection("YEAR(this_.c_date) as year, MONTH(this_.c_date) as month, SUM(this_.c_sum_payed) as sumPayed, SUM(this_.c_sum_to_pay) as sumToPay", "YEAR(this_.c_date), MONTH(this_.c_date)", new String[] {
                "year", "month", "sumPayed", "sumToPay"}, new Type[] {Hibernate.LONG, Hibernate.LONG, Hibernate.DOUBLE,
                Hibernate.DOUBLE})));

        List<Object[]> list = criteria.list();

        List<AggregatedExpensesDto> res = new ArrayList<AggregatedExpensesDto>(list.size());
        for (Object[] objects : list) {
            AggregatedExpensesDto resItem = new AggregatedExpensesDto();
            resItem.setObjectId(null);
            resItem.setYear((Long) objects[0]);
            resItem.setMonth((Long) objects[1] - 1);
            resItem.setSumPayed((Double) objects[2]);
            resItem.setSumToPay((Double) objects[3]);
            res.add(resItem);
        }
        return res;
    }

    public Expense save(ExpenseDto dto) throws AnyServiceException {
        Expense entity = null;
        if (dto.getId() == null)
            entity = new Expense();
        else {
            entity = (Expense) findById(dto.getId());
        }

        if (dto.getService() == null) throw new AnyServiceException("Не задана услугу");
        ExpenseService service = expenseServiceDao.findByName(dto.getService());
        if (service == null) {
            service = new ExpenseService();
            service.setName(dto.getService());
        }
        entity.setService(service);

        entity.setObject(em.find(LandObject.class, dto.getObjectId()));

        if (dto.getSumToPay() == null) throw new AnyServiceException("Не задана сумма к оплате");
        entity.setSumToPay(dto.getSumToPay());

        if (dto.getSumPayed() == null) throw new AnyServiceException("Не задана оплаченная сумма");
        entity.setSumPayed(dto.getSumPayed());

        entity.setDate(dto.getDate());
        if (dto.getDate() == null) throw new AnyServiceException("Не задана дата");

        File receipt = null;
        if (dto.getReceipt() != null) receipt = em.find(File.class, dto.getReceipt().getId());
        entity.setReceipt(receipt);

        persist(entity);

        return entity;
    }

    public ObjectYearsBoundariesDto getObjectExpenseBoundaries(ObjectQBE qbe) {
        ObjectYearsBoundariesDto res = new ObjectYearsBoundariesDto();
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.setProjection(Projections.projectionList().add(Projections.sqlProjection("MIN(YEAR(this_.c_date)) as minYear, MAX(YEAR(this_.c_date)) as maxYear", new String[] {
                "minYear", "maxYear"}, new Type[] {Hibernate.LONG, Hibernate.LONG})));
        Object[] minmax = (Object[]) criteria.uniqueResult();
        if (minmax != null) {
            res.setMinYear((Long) minmax[0]);
            res.setMaxYear((Long) minmax[1]);
        }
        res.setObjectCount(objectDao.getObjectsCount(qbe));

        return res;
    }

    public List<ExpenseDto> selectExpenses(ExpenseQBE qbe) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        if (qbe.getYear() != null)
            criteria.add(Restrictions.sqlRestriction("YEAR(this_.c_date) = ?", qbe.getYear(), Hibernate.LONG));

        if (qbe.getObjectIdList() != null) {
            if (qbe.getObjectIdList().isEmpty()) {
                criteria.add(Restrictions.isNull("id"));
            }
            else
                criteria.add(Restrictions.in("object.id", qbe.getObjectIdList()));
        }

        criteria.setFirstResult(qbe.getFirst());
        if (qbe.getCount() > 0) criteria.setMaxResults(qbe.getCount());

        List<Expense> list = criteria.list();
        List<ExpenseDto> res = new ArrayList<ExpenseDto>(list.size());
        for (Expense expense : list)
            res.add(expense.toDto());

        return res;
    }

}
