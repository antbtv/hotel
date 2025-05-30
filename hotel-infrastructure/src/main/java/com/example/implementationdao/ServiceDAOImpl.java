package com.example.implementationdao;

import com.example.dbconnection.HibernateSessionFactory;
import com.example.dao.ServiceDAO;
import com.example.entity.Guest;
import com.example.entity.ProvidedService;
import com.example.entity.Service;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ServiceDAOImpl implements ServiceDAO {

    private static final String SELECT_ALL_SERVICES = "from Service";

    private final HibernateSessionFactory hibernateSessionFactory;

    private static final Logger logger = LogManager.getLogger(ServiceDAO.class);

    public ServiceDAOImpl(HibernateSessionFactory hibernateSessionFactory) {
        this.hibernateSessionFactory = hibernateSessionFactory;
    }

    @Override
    public void create(Service service) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.persist(service);
            logger.debug(MessageSources.SUCCESS_CREATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_CREATE, e);
        }
    }

    @Override
    public Service read(int id) {
        Service service = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            service = session.get(Service.class, id);
            logger.debug(MessageSources.SUCCESS_READ_ONE);
            logger.debug("Цена: " + service.getPrice());
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_ONE, e);
        }
        return service;
    }

    @Override
    public void update(Service service) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            session.merge(service);
            logger.debug(MessageSources.SUCCESS_UPDATE);
            logger.debug("Цена: " + service.getPrice());
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_UPDATE, e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            ;
            Service service = session.get(Service.class, id);
            if (service != null) {
                session.remove(service);
                logger.debug(MessageSources.SUCCESS_DELETE);
            }
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_DELETE, e);
        }
    }

    @Override
    public List<Service> findAll() {
        List<Service> services = null;
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            services = session.createQuery(SELECT_ALL_SERVICES, Service.class).list();
            logger.debug(MessageSources.SUCCESS_READ_MANY);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_READ_MANY, e);
        }
        return services;
    }

    @Override
    public void addServiceForGuest(Guest guest, Service service, LocalDate date) {
        try {
            Session session = hibernateSessionFactory.getCurrentSession();
            ProvidedService providedService = new ProvidedService();
            providedService.setGuest(guest);
            providedService.setService(service);
            providedService.setDate(date);
            session.persist(providedService);

            logger.debug(MessageSources.SUCCESS_CREATE);
        } catch (Exception e) {
            logger.error(MessageSources.FAILURE_CREATE, e);
        }
    }
}