package com.example.implementationdao;

import com.example.dbconnection.HibernateSessionFactory;
import com.example.dao.RoomDAO;
import com.example.entity.Room;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAOImpl implements RoomDAO {

    private static final String SELECT_ALL_ROOMS = "from Room";
    private static final String SELECT_ROOM = "SELECT r FROM Room r LEFT JOIN FETCH r.guests WHERE r.roomId = :roomId";

    private final HibernateSessionFactory hibernateSessionFactory;

    private static final Logger logger = LogManager.getLogger(RoomDAO.class);

    public RoomDAOImpl(HibernateSessionFactory hibernateSessionFactory) {
        this.hibernateSessionFactory = hibernateSessionFactory;
    }

    @Override
    public void create(Room room) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.persist(room);
            logger.debug(MessageSources.SUCCESS_CREATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_CREATE, e);
        }
    }

    @Override
    public Room read(int id) {
        Room room = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            room = session.createQuery(SELECT_ROOM, Room.class)
                    .setParameter("roomId", id)
                    .uniqueResult();
            logger.debug(MessageSources.SUCCESS_READ_ONE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_ONE, e);
        }
        return room;
    }

    @Override
    public void update(Room room) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.merge(room);
            logger.debug(MessageSources.SUCCESS_UPDATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_UPDATE, e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            Room room = session.get(Room.class, id);
            if (room != null) {
                session.remove(room);
                logger.debug(MessageSources.SUCCESS_DELETE);
            }
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_DELETE, e);
        }
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            rooms = session.createQuery(SELECT_ALL_ROOMS, Room.class).list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return rooms;
    }
}
