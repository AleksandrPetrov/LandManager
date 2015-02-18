package com.land.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.land.server.domain.User;
import com.land.server.security.CurrentUser;
import com.land.shared.dto.UserDto;
import com.land.shared.dto.qbe.UserQBE;
import com.land.shared.exception.AnyServiceException;
import com.land.shared.exception.ValidationException;

@Repository
public class UserDao extends JpaDao<User> {

    private static final int MIN_PASSWORD_LENGTH = 3;

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    public User findByLogin(String login) {
        if (login == null) return null;
        try {
            login = login.trim().toLowerCase();
            Query q = em.createQuery("SELECT e FROM " + getEntityClass().getName() + " e WHERE e.login = :login");
            q.setParameter("login", login);
            return (User) q.getSingleResult();
        }
        catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public User save(UserDto bean, boolean checkOldPassword) throws AnyServiceException {
        assertLength(bean.getName(), 1024, "Полное имя");
        assertLength(bean.getEmail(), 1024, "Email");
        assertLength(bean.getLogin(), 255, "Логин");
        assertLength(bean.getPasswordNew(), 255, "Пароль");

        if (bean.getId() == null) {
            if (findByLogin(bean.getLogin()) != null)
                throw new ValidationException("Пользователь с таким логином уже существует");
            assertNull(bean.getPasswordNew(), "Пароль");
            if (bean.getPasswordNew().trim().length() < MIN_PASSWORD_LENGTH)
                throw new ValidationException("Длина пароля не должна быть меньше " + MIN_PASSWORD_LENGTH + " символов");
        }

        User entity = null;
        if (bean.getId() == null) {
            entity = new User();
            entity.setPassword(bean.getPasswordNew().trim());
        }
        else {
            entity = (User) findById(bean.getId());
            assertEntity(entity, "Пользователь");

            if (bean.getPasswordNew() != null && !bean.getPasswordNew().isEmpty()) {
                if (checkOldPassword && !entity.getPassword().equals(bean.getPasswordOld().trim()))
                    throw new ValidationException("Неправильный пароль! Для изменения пароля необходимо ввести текущий пароль.");

                if (bean.getPasswordNew().trim().length() < MIN_PASSWORD_LENGTH)
                    throw new ValidationException("Длина пароля не должна быть меньше " + MIN_PASSWORD_LENGTH
                            + " символов");
                entity.setPassword(bean.getPasswordNew());
            }
        }

        entity.setLogin(bean.getLogin().trim());
        entity.setName(bean.getName() == null ? "" : bean.getName().trim());
        entity.setEmail(bean.getEmail() == null ? "" : bean.getEmail().trim());

        persist(entity);

        return entity;
    }

    public Long getUsersCount(UserQBE qbe) throws AnyServiceException {
        Criteria criteria = buildCriteria(qbe);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<User> getListUser(UserQBE qbe) throws AnyServiceException {
        Criteria criteria = buildCriteria(qbe);
        criteria.setFirstResult(qbe.getFirst());
        criteria.setMaxResults(qbe.getCount());
        return criteria.list();
    }

    private Criteria buildCriteria(UserQBE qbe) {
        Criteria criteria = getHibernateSession().createCriteria(User.class);
        criteria.add(Restrictions.ne("id", CurrentUser.getId()));

        if (qbe.getFilter() != null) {
            String filter = "%" + qbe.getFilter() + "%";
            criteria.add(Restrictions.disjunction().add(Restrictions.like("login", filter)).add(Restrictions.like("name", filter)).add(Restrictions.like("email", filter)));
        }

        return criteria;
    }

    public List<UserDto> getUsersDto(UserQBE qbe) throws AnyServiceException {
        List<UserDto> res = new ArrayList<UserDto>();
        List<User> list = getListUser(qbe);
        for (User user : list) {
            res.add(user.toDto());
        }
        return res;
    }

}
