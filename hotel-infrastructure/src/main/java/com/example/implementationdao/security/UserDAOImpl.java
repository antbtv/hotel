package com.example.implementationdao.security;

import com.example.dao.security.UserDAO;
import com.example.dbconnection.HibernateSessionFactory;
import com.example.entity.security.User;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private static final String SELECT_ALL_USERS = "from User";
    private static final String SELECT_BY_USERNAME = "FROM User WHERE username = :username";

    private final HibernateSessionFactory hibernateSessionFactory;

    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    public UserDAOImpl(HibernateSessionFactory hibernateSessionFactory) {
        this.hibernateSessionFactory = hibernateSessionFactory;
    }

    @Override
    public void create(User user) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.persist(user);
            logger.debug(MessageSources.SUCCESS_CREATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_CREATE, e);
        }
    }

    @Override
    public User read(int id) {
        User user = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            user = session.get(User.class, id);
            logger.debug(MessageSources.SUCCESS_READ_ONE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_ONE, e);
        }
        return user;
    }

    @Override
    public void update(User user) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.merge(user);
            logger.debug(MessageSources.SUCCESS_UPDATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_UPDATE, e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.debug(MessageSources.SUCCESS_DELETE);
            }
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_DELETE, e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            users = session.createQuery(SELECT_ALL_USERS, User.class).list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return users;
    }

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            user = session.createQuery(SELECT_BY_USERNAME, User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            logger.debug(MessageSources.SUCCESS_READ_ONE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_ONE, e);
        }
        return user;
    }
}