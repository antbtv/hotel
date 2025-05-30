package com.example.dao;

import com.example.entity.ProvidedService;
import com.example.entity.Service;

import java.util.List;

public interface ProvidedServicesDAO extends GenericDAO<ProvidedService> {

    /**
     * Получение всех услуг определённого гостя
     *
     * @param guestId id гостя
     * @return список услуг
     */
    List<Service> getServicesForGuest(int guestId);
}