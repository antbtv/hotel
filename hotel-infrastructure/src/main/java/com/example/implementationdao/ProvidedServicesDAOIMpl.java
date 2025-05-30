package com.example.implementationdao;

import com.example.dbconnection.HibernateSessionFactory;
import com.example.dao.ProvidedServicesDAO;
import com.example.entity.ProvidedService;
import com.example.entity.Service;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProvidedServicesDAOIMpl implements ProvidedServicesDAO {

    private static final String SELECT_ALL_GUEST_SERVICES = "from ProvidedService";
    private static final String SELECT_SERVICES_FOR_GUEST = "SELECT gs.service FROM ProvidedService gs JOIN gs.guest g WHERE g.guestId = :guestId";

    private final HibernateSessionFactory hibernateSessionFactory;

    private static final Logger logger = LogManager.getLogger(ProvidedServicesDAO.class);

    public ProvidedServicesDAOIMpl(HibernateSessionFactory hibernateSessionFactory) {
        this.hibernateSessionFactory = hibernateSessionFactory;
    }

    @Override
    public void create(ProvidedService providedService) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.persist(providedService);
            logger.debug(MessageSources.SUCCESS_CREATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_CREATE, e);
        }
    }

    @Override
    public ProvidedService read(int id) {
        ProvidedService providedService = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            providedService = session.get(ProvidedService.class, id);
            logger.debug(MessageSources.SUCCESS_READ_ONE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_ONE, e);
        }
        return providedService;
    }

    @Override
    public void update(ProvidedService providedService) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.merge(providedService);
            logger.debug(MessageSources.SUCCESS_UPDATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_UPDATE, e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            ProvidedService providedService = session.get(ProvidedService.class, id);
            if (providedService != null) {
                session.remove(providedService);
                logger.debug(MessageSources.SUCCESS_DELETE);
            }
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_DELETE, e);
        }
    }

    @Override
    public List<ProvidedService> findAll() {
        List<ProvidedService> providedServices = new ArrayList<>();
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            providedServices = session.createQuery(SELECT_ALL_GUEST_SERVICES, ProvidedService.class).list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return providedServices;
    }

    @Override
    public List<Service> getServicesForGuest(int guestId) {
        List<Service> services = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            services = session.createQuery(SELECT_SERVICES_FOR_GUEST, Service.class)
                    .setParameter("guestId", guestId).list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return services;
    }
}