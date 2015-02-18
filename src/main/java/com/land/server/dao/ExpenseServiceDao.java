package com.land.server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.land.server.domain.ExpenseService;
import com.land.shared.exception.AnyServiceException;

@SuppressWarnings({"unchecked"})
@Repository
public class ExpenseServiceDao extends JpaDao<ExpenseService> {

    @Override
    public Class<ExpenseService> getEntityClass() {
        return ExpenseService.class;
    }

    public ExpenseService findByName(String name) throws AnyServiceException {
        if (name == null) return null;
        name = name.trim();
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.add(Restrictions.eq("name", name));
        return (ExpenseService) criteria.uniqueResult();
    }

    public List<String> selectAllNames() {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.setProjection(Projections.property("name"));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }
}
