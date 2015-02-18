package com.land.server.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.land.server.domain.BlobEntity;
import com.land.server.domain.File;
import com.land.shared.dto.qbe.FileQBE;

@SuppressWarnings("unchecked")
@Repository
public class FileDao extends JpaDao<File> {

    @Override
    public Class<File> getEntityClass() {
        return File.class;
    }

    public BlobEntity findBlobByFileId(Long id) {
        if (id == null) return null;
        Query q = em.createQuery("SELECT b FROM BlobEntity b WHERE b.file.id = " + id);
        return (BlobEntity) q.getResultList().get(0);
    }

    public Long getFileCount(FileQBE qbe) {
        Criteria criteria = buildCriteria(qbe);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    public List<File> selectFiles(FileQBE qbe) {
        Criteria criteria = buildCriteria(qbe);
        criteria.setFirstResult(qbe.getFirst());
        if (qbe.getCount() > 0) criteria.setMaxResults(qbe.getCount());
        return criteria.list();
    }

    private Criteria buildCriteria(FileQBE qbe) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.addOrder(Order.asc("name"));
        if (qbe.getObjectId() != null) criteria.add(Restrictions.eq("object.id", qbe.getObjectId()));
        return criteria;
    }
}
