package com.example.service.impl;

import com.example.dao.ProvidedServicesDAO;
import com.example.dao.ServiceDAO;
import com.example.entity.ProvidedService;
import com.example.entity.Service;
import com.example.service.ServiceService;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceDAO serviceDAO;
    private final ProvidedServicesDAO providedServicesDAO;

    private static final Logger logger = LogManager.getLogger(ServiceServiceImpl.class);

    @Autowired
    public ServiceServiceImpl(ServiceDAO serviceDAO,
                              ProvidedServicesDAO providedServicesDAO) {
        this.serviceDAO = serviceDAO;
        this.providedServicesDAO = providedServicesDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public Service getService(int id) {
        return serviceDAO.read(id);
    }

    @Override
    @Transactional
    public void setNewServicePrice(int id, int newPrice) {
        Service service = getService(id);
        if (service != null) {
            service.setPrice(newPrice);
            serviceDAO.update(service);
        } else {
            logger.error(MessageSources.NOT_EXIST);
        }
    }

    @Override
    @Transactional
    public Service addNewService(String serviceName, int price) {
        Service service = new Service(serviceName, price);
        serviceDAO.create(service);
        return service;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> getServices() {
        return serviceDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvidedService> getProvidedServices() {
        return providedServicesDAO.findAll();
    }

    @Override
    public String getServiceInfo(Service service) {
        return service.toString();
    }

    @Override
    public List<Service> sortServicesBy(List<Service> services, Comparator<Service> comparator) {
        services.sort(comparator);
        return services;
    }

    @Override
    public List<ProvidedService> sortProvidedServicesBy(List<ProvidedService> providedServices,
                                                        Comparator<ProvidedService> comparator) {
        providedServices.sort(comparator);
        return providedServices;
    }

    @Override
    @Transactional
    public void importServices(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String serviceName = data[0];
                int price = Integer.parseInt(data[1]);
                addNewService(serviceName, price);
            }
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_FILE_READ);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public File exportServices() {
        List<Service> services = serviceDAO.findAll();
        File file = new File("services_export.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Service service : services) {
                String serviceInfo = getServiceInfo(service);
                writer.write(serviceInfo);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_WRITE);
        }

        return file;
    }

    @Override
    @Transactional
    public void deleteService(int serviceId) {
        serviceDAO.delete(serviceId);
    }
}
