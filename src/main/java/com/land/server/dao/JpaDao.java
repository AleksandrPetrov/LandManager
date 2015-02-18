package com.land.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.land.shared.exception.AnyServiceException;
import com.land.shared.exception.ValidationException;

@Repository
public abstract class JpaDao<E> {

    public static final Logger log = Logger.getLogger(JpaDao.class);

    public abstract Class<E> getEntityClass();

    @PersistenceContext
    protected EntityManager em;

    protected Session getHibernateSession() {
        return (Session) em.getDelegate();
    }

    @Transactional
    public void persist(Object entity) throws AnyServiceException {
        log.debug(getEntityClass().getSimpleName() + " persist: ");
        try {
            em.persist(entity);
            em.flush();
            // System.out.println("id " + entity.getId());
        }
        catch (Exception e) {
            log.error("persist");
            throw new AnyServiceException("Ошибка сохранения. Возможно не все обязательные поля заполнены", e);
        }
    }

    @Transactional
    public void remove(E entity) throws AnyServiceException {
        log.debug(getEntityClass().getSimpleName() + " remove: ");
        em.remove(entity);
    }

    @Transactional
    public void remove(Long id) throws AnyServiceException {
        log.debug(getEntityClass().getSimpleName() + " remove: " + id);
        em.remove(findById(id));
    }

    public E findById(Long id) {
        if (id == null) return null;
        log.debug(getEntityClass().getSimpleName() + " findById: " + id);
        return em.find(getEntityClass(), id);
    }

    @SuppressWarnings("unchecked")
    public List<E> selectAll() throws AnyServiceException {
        log.debug(getEntityClass().getSimpleName() + " selectAll");
        Query q = em.createQuery("SELECT e FROM " + getEntityClass().getName() + " e");
        return q.getResultList();
    }

    public void assertLength(String string, int length, String name) throws AnyServiceException {
        if (string != null && string.length() > length)
            throw new ValidationException("Длина атрибута " + name + " превышает максимальное значение " + length);
    }

    public void assertNull(String string, String name) throws AnyServiceException {
        if (string == null || string.trim().isEmpty())
            throw new ValidationException("Значение атрибута " + name + " не задано");
    }

    public void assertNull(Object object, String name) throws AnyServiceException {
        if (object == null) throw new ValidationException("Значение атрибута " + name + " не задано");
    }

    public void assertEntity(Object object, String name) throws AnyServiceException {
        if (object == null)
            throw new AnyServiceException("Объект " + name + "(" + getEntityClass().toString() + ") в базе не найден");
    }
}
