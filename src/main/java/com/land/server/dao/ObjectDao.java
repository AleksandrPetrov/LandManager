package com.land.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.land.server.domain.LandObject;
import com.land.shared.dto.ObjectDto;
import com.land.shared.dto.qbe.ObjectQBE;
import com.land.shared.exception.AnyServiceException;

@SuppressWarnings("unchecked")
@Repository
public class ObjectDao extends JpaDao<LandObject> {

    @Override
    public Class<LandObject> getEntityClass() {
        return LandObject.class;
    }

    public Long getObjectsCount(ObjectQBE qbe) {
        Criteria criteria = buildCriteria(qbe);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    public List<LandObject> selectObjects(ObjectQBE qbe) {
        Criteria criteria = buildCriteria(qbe);
        criteria.addOrder(Order.asc("address"));
        criteria.setFirstResult(qbe.getFirst());
        if (qbe.getCount() > 0) criteria.setMaxResults(qbe.getCount());
        return criteria.list();
    }

    public List<ObjectDto> selectObjectsDto(ObjectQBE qbe) {
        Criteria criteria = buildCriteria(qbe);
        criteria.addOrder(Order.asc("address"));
        criteria.setFirstResult(qbe.getFirst());
        if (qbe.getCount() > 0) criteria.setMaxResults(qbe.getCount());

        List<LandObject> list = criteria.list();
        List<ObjectDto> res = new ArrayList<ObjectDto>(list.size());
        for (LandObject landObject : list)
            res.add(landObject.toDto());
        return res;
    }

    private Criteria buildCriteria(ObjectQBE qbe) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        if (qbe.getExpencesView() != null) {
            criteria.add(Restrictions.eq("expencesView", qbe.getExpencesView()));
        }
        if (qbe.getTaxesView() != null) {
            criteria.add(Restrictions.eq("taxesView", qbe.getTaxesView()));
        }
        if (qbe.getIncomesView() != null) {
            criteria.add(Restrictions.eq("incomesView", qbe.getIncomesView()));
        }
        return criteria;
    }

    public LandObject save(ObjectDto dto) throws AnyServiceException {
        LandObject entity = null;
        if (dto.getId() == null)
            entity = new LandObject();
        else {
            entity = (LandObject) findById(dto.getId());
        }

        entity.setAddress(dto.getAddress());
        entity.setTaxesView(dto.getTaxesView());
        entity.setExpencesView(dto.getExpencesView());
        entity.setIncomesView(dto.getIncomesView());

        persist(entity);

        return entity;
    }

}
