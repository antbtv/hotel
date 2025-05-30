package com.example.service;

import com.example.entity.ProvidedService;
import com.example.entity.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * Интерфейс для управления сервисами
 */
public interface ServiceService {

    /**
     * Поиск сервиса по названию
     *
     * @param id id сервиса
     * @return возвращает объект сервиса
     */
    Service getService(int id);

    /**
     * Смена цены сервиса
     *
     * @param id сервис
     * @param newPrice новая цена
     */
    void setNewServicePrice(int id, int newPrice);

    /**
     * Добавление нового сервиса
     *
     * @param serviceName имя сервиса
     * @param price цена
     * @return созданный сервис
     */
    Service addNewService(String serviceName, int price);

    /**
     * Получение списка сервисов
     *
     * @return список сервисов
     */
    List<Service> getServices();

    /**
     * Получение списка исполненных сервисов
     *
     * @return список исполненных сервисов
     */
    List<ProvidedService> getProvidedServices();

    /**
     * Получение информации заданного сервиса
     *
     * @param service сервис
     * @return возвращение информации
     */
    String getServiceInfo(Service service);

    /**
     * Получение отсортированного списка сервисов
     *
     * @param services исходный список сервисов
     * @param comparator параметр сортировки
     * @return отсортированный список
     */
    List<Service> sortServicesBy(List<Service> services, Comparator<Service> comparator);

    /**
     * Получение отсортированного списка сервисов по дате.
     * Для этого используем сущность ProvidedServices,
     * которая хранит в себе дату исполнения услуги.
     *
     * @param providedServices исходный список оказанных услуг
     * @param comparator параметр сортировки
     * @return отсортированный список
     */
    List<ProvidedService> sortProvidedServicesBy(List<ProvidedService> providedServices, Comparator<ProvidedService> comparator);

    /**
     * Импорт сервисов из файла
     *
     * @param file файл
     */
    void importServices(MultipartFile file);

    /**
     * Экспорт сервисов в файл
     *
     * @return файл со списком сервисов
     */
    File exportServices();

    /**
     * Удаление сервиса по id
     *
     * @param serviceId id сервиса
     */
    void deleteService(int serviceId);
}
