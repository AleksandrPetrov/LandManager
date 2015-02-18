package com.land.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.land.server.domain.Income;
import com.land.server.domain.LandObject;
import com.land.shared.dto.AggregatedIncomeDto;
import com.land.shared.dto.IncomeDto;
import com.land.shared.dto.ObjectYearsBoundariesDto;
import com.land.shared.dto.qbe.AggregatedIncomesQBE;
import com.land.shared.dto.qbe.ObjectIncomeQBE;
import com.land.shared.dto.qbe.ObjectQBE;
import com.land.shared.exception.AnyServiceException;

@SuppressWarnings({"unchecked"})
@Repository
public class IncomeDao extends JpaDao<Income> {

    @Autowired
    private ObjectDao objectDao;

    @Override
    public Class<Income> getEntityClass() {
        return Income.class;
    }

    public List<AggregatedIncomeDto> selectTotalAggregatedIncomes(AggregatedIncomesQBE qbe, List<Long> objectIdList) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        if (qbe.getYear() != null) criteria.add(Restrictions.eq("year", qbe.getYear()));
        if (objectIdList != null) {
            if (objectIdList.isEmpty()) {
                criteria.add(Restrictions.isNull("id"));
            }
            else
                criteria.add(Restrictions.in("object.id", objectIdList));
        }
        criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("year")).add(Projections.groupProperty("month")).add(Projections.sum("value")));

        List<Object[]> list = criteria.list();

        List<AggregatedIncomeDto> res = new ArrayList<AggregatedIncomeDto>(list.size());
        for (Object[] objects : list) {
            AggregatedIncomeDto resItem = new AggregatedIncomeDto();
            resItem.setObjectId(null);
            resItem.setYear((Long) objects[0]);
            resItem.setMonth((Long) objects[1]);
            resItem.setSum((Double) objects[2]);
            res.add(resItem);
        }
        return res;
    }

    public List<IncomeDto> selectIncomes(ObjectIncomeQBE qbe) {
        Criteria criteria = buildCriteria(qbe);

        criteria.setFirstResult(qbe.getFirst());
        if (qbe.getCount() > 0) criteria.setMaxResults(qbe.getCount());

        List<Income> list = criteria.list();
        List<IncomeDto> res = new ArrayList<IncomeDto>(list.size());
        for (Income objects : list)
            res.add(objects.toDto());

        return res;
    }

    private Criteria buildCriteria(ObjectIncomeQBE qbe) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        if (qbe.getYear() != null) {
            criteria.add(Restrictions.eq("year", qbe.getYear()));
        }
        if (qbe.getObjectIdList() != null) {
            if (qbe.getObjectIdList().isEmpty()) {
                criteria.add(Restrictions.isNull("id"));
            }
            else
                criteria.add(Restrictions.in("object.id", qbe.getObjectIdList()));
        }
        return criteria;
    }

    public Income save(IncomeDto dto) throws AnyServiceException {
        Income entity = null;
        if (dto.getId() == null)
            entity = new Income();
        else {
            entity = (Income) findById(dto.getId());
        }

        entity.setObject(em.find(LandObject.class, dto.getObjectId()));
        entity.setValue(dto.getValue());
        entity.setYear(dto.getYear());
        entity.setMonth(dto.getMonth());

        try {
            persist(entity);
        }
        catch (Exception e) {
            throw new AnyServiceException("Доход за этот месяц уже существует.");
        }

        return entity;
    }

    public ObjectYearsBoundariesDto getObjectIncomeBoundaries(ObjectQBE qbe) {
        ObjectYearsBoundariesDto res = new ObjectYearsBoundariesDto();
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.setProjection(Projections.projectionList().add(Projections.min("year")).add(Projections.max("year")));
        Object[] minmax = (Object[]) criteria.uniqueResult();
        if (minmax != null) {
            res.setMinYear((Long) minmax[0]);
            res.setMaxYear((Long) minmax[1]);
        }
        res.setObjectCount(objectDao.getObjectsCount(qbe));

        return res;
    }

}
