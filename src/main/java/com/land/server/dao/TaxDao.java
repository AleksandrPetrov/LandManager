package com.land.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.land.server.domain.File;
import com.land.server.domain.LandObject;
import com.land.server.domain.Tax;
import com.land.shared.dto.AggregatedTaxDto;
import com.land.shared.dto.TaxDto;
import com.land.shared.dto.qbe.ObjectQBE;
import com.land.shared.exception.AnyServiceException;

@SuppressWarnings("unchecked")
@Repository
public class TaxDao extends JpaDao<Tax> {

    @Override
    public Class<Tax> getEntityClass() {
        return Tax.class;
    }

    public Long getObjectsCount(ObjectQBE qbe) {
        Criteria criteria = buildCriteria(qbe);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    public List<Tax> selectObjects(ObjectQBE qbe) {
        Criteria criteria = buildCriteria(qbe);
        criteria.setFirstResult(qbe.getFirst());
        if (qbe.getCount() > 0) criteria.setMaxResults(qbe.getCount());
        return criteria.list();
    }

    private Criteria buildCriteria(ObjectQBE qbe) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.addOrder(Order.asc("address"));
        return criteria;
    }

    public Tax save(TaxDto dto) throws AnyServiceException {
        Tax entity = null;
        if (dto.getId() == null)
            entity = new Tax();
        else {
            entity = (Tax) findById(dto.getId());
        }

        entity.setObject(em.find(LandObject.class, dto.getObjectId()));
        entity.setValue(dto.getValue());
        entity.setYear(dto.getYear());

        File receipt = null;
        if (dto.getReceipt() != null) receipt = em.find(File.class, dto.getReceipt().getId());
        entity.setReceipt(receipt);

        try {
            persist(entity);
        }
        catch (Exception e) {
            throw new AnyServiceException("Налог за этот год уже существует.");
        }

        return entity;
    }

    public List<AggregatedTaxDto> selectTotalAggregatedTaxes(List<Long> objectIdList) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        if (objectIdList != null) {
            if (objectIdList.isEmpty()) {
                criteria.add(Restrictions.isNull("id"));
            }
            else
                criteria.add(Restrictions.in("object.id", objectIdList));
        }
        criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("year")).add(Projections.sum("value")));

        List<Object[]> list = criteria.list();

        List<AggregatedTaxDto> res = new ArrayList<AggregatedTaxDto>(list.size());
        for (Object[] objects : list) {
            AggregatedTaxDto resItem = new AggregatedTaxDto();
            resItem.setObjectId(null);
            resItem.setYear((Integer) objects[0]);
            resItem.setSum((Double) objects[1]);
            res.add(resItem);
        }
        return res;
    }

}
