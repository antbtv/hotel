package com.example.dao;

import com.example.entity.Guest;
import com.example.entity.Service;

import java.time.LocalDate;

public interface ServiceDAO extends GenericDAO<Service> {

    /**
     * Добавление услуги
     *
     * @param service сервис
     * @param guest гость
     * @param date дата оказания услуги
     */
    void addServiceForGuest(Guest guest, Service service, LocalDate date);
}