package com.example.implementationdao.security;

import com.example.dao.security.PrivilegeDAO;
import com.example.dbconnection.HibernateSessionFactory;
import com.example.entity.security.Privilege;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PrivilegeDAOImpl implements PrivilegeDAO {

    private static final String SELECT_ALL_PRIVILEGES = "from Privilege";

    private final HibernateSessionFactory hibernateSessionFactory;

    private static final Logger logger = LogManager.getLogger(PrivilegeDAOImpl.class);

    public PrivilegeDAOImpl(HibernateSessionFactory hibernateSessionFactory) {
        this.hibernateSessionFactory = hibernateSessionFactory;
    }

    @Override
    public void create(Privilege privilege) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.persist(privilege);
            logger.debug(MessageSources.SUCCESS_CREATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_CREATE, e);
        }
    }

    @Override
    public Privilege read(int id) {
        Privilege privilege = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            privilege = session.get(Privilege.class, id);
            logger.debug(MessageSources.SUCCESS_READ_ONE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_ONE, e);
        }
        return privilege;
    }

    @Override
    public void update(Privilege privilege) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.merge(privilege);
            logger.debug(MessageSources.SUCCESS_UPDATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_UPDATE, e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            Privilege privilege = session.get(Privilege.class, id);
            if (privilege != null) {
                session.remove(privilege);
                logger.debug(MessageSources.SUCCESS_DELETE);
            }
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_DELETE, e);
        }
    }

    @Override
    public List<Privilege> findAll() {
        List<Privilege> privileges = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            privileges = session.createQuery(SELECT_ALL_PRIVILEGES, Privilege.class).list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return privileges;
    }
}