package com.example.implementationdao;

import com.example.dbconnection.HibernateSessionFactory;
import com.example.dao.RoomHistoryDAO;
import com.example.entity.RoomHistory;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomHistoryDAOImpl implements RoomHistoryDAO {

    private static final String SELECT_ALL_ROOM_HISTORY = "from RoomHistory";

    private final HibernateSessionFactory hibernateSessionFactory;

    private static final Logger logger = LogManager.getLogger(RoomHistoryDAO.class);

    public RoomHistoryDAOImpl(HibernateSessionFactory hibernateSessionFactory) {
        this.hibernateSessionFactory = hibernateSessionFactory;
    }

    @Override
    public void create(RoomHistory roomHistory) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.persist(roomHistory);
            logger.debug(MessageSources.SUCCESS_CREATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_CREATE, e);
        }
    }

    @Override
    public RoomHistory read(int id) {
        RoomHistory roomHistory = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            roomHistory = session.get(RoomHistory.class, id);
            logger.debug(MessageSources.SUCCESS_READ_ONE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_ONE, e);
        }
        return roomHistory;
    }

    @Override
    public void update(RoomHistory roomHistory) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.merge(roomHistory);
            logger.debug(MessageSources.SUCCESS_UPDATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_UPDATE, e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            RoomHistory roomHistory = session.get(RoomHistory.class, id);
            if (roomHistory != null) {
                session.remove(roomHistory);
                logger.debug(MessageSources.SUCCESS_DELETE);
            }
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_DELETE, e);
        }
    }

    @Override
    public List<RoomHistory> findAll() {
        List<RoomHistory> historyList = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            historyList = session.createQuery(SELECT_ALL_ROOM_HISTORY, RoomHistory.class).list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return historyList;
    }
}