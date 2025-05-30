package com.example.implementationdao;

import com.example.dbconnection.HibernateSessionFactory;
import com.example.dao.GuestDAO;
import com.example.entity.Guest;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GuestDAOImpl implements GuestDAO {

    private static final String SELECT_ALL_GUESTS = "from Guest";
    private static final String SELECT_GUSTS_BY_IDS = "FROM Guest g WHERE g.guestId IN :guestIds";
    private static final String SELECT_LAST_GUESTS =
            "SELECT g FROM Guest g JOIN RoomHistory rh ON g.guestId = rh.guestId " +
                    "WHERE rh.roomId = :roomId ORDER BY rh.checkOutDate DESC";
    private static final String SELECT_GUEST =
            "SELECT g FROM Guest g LEFT JOIN FETCH g.room WHERE g.guestId = :guestId";

    private final HibernateSessionFactory hibernateSessionFactory;

    private static final Logger logger = LogManager.getLogger(GuestDAO.class);

    public GuestDAOImpl(HibernateSessionFactory hibernateSessionFactory) {
        this.hibernateSessionFactory = hibernateSessionFactory;
    }

    @Override
    public void create(Guest guest) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.persist(guest);
            logger.debug(MessageSources.SUCCESS_CREATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_CREATE, e);
        }
    }

    @Override
    public Guest read(int id) {
        System.out.println("guestdao");
        Guest guest = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            guest = session.createQuery(SELECT_GUEST, Guest.class)
                    .setParameter("guestId", id)
                    .uniqueResult();
            logger.debug(MessageSources.SUCCESS_READ_ONE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_ONE, e);
        }
        return guest;
    }

    @Override
    public void update(Guest guest) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.merge(guest);
            logger.debug(MessageSources.SUCCESS_UPDATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_UPDATE, e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            Guest guest = session.get(Guest.class, id);
            if (guest != null) {
                session.remove(guest);
                logger.debug(MessageSources.SUCCESS_DELETE);
            }
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_DELETE, e);
        }
    }

    @Override
    public List<Guest> findAll() {
        List<Guest> guests = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            guests = session.createQuery(SELECT_ALL_GUESTS, Guest.class).list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return guests;
    }

    @Override
    public List<Guest> getLastThreeGuests(int roomId) {
        List<Guest> guests = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            guests = session.createQuery(SELECT_LAST_GUESTS, Guest.class)
                    .setParameter("roomId", roomId)
                    .setMaxResults(3)
                    .list();
            logger.debug("Получены 3 последних гостя");
        } catch (Exception e) {
            logger.error("Ошибка при получении последних трех гостей для номера " + roomId, e);
        }
        return guests;
    }

    @Override
    public List<Guest> findByIds(List<Integer> guestIds) {
        List<Guest> guests = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            guests = session.createQuery(SELECT_GUSTS_BY_IDS, Guest.class)
                    .setParameter("guestIds", guestIds)
                    .list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return guests;
    }
}