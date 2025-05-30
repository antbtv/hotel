package com.example.service;

import com.example.entity.Guest;
import com.example.entity.Hotel;
import com.example.entity.ProvidedService;
import com.example.entity.Room;
import com.example.entity.Service;
import com.example.enums.Status;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Интерфейс для управления отелем
 */
public interface HotelManager {

    /**
     * Получение объекта для экспорта/импорта в/из
     *
     * json файла
     * @return объект Hotel, хранящий все данные об отеле
     */
    Hotel getHotel();

    /**
     * Поиск гостя по имени
     *
     * @param id id гостя
     * @return возвращает объект гостя
     */
    Guest getGuest(int id);

    /**
     * Поиск номера по числовому значению
     *
     * @param id id номера
     * @return возвращает объект номера
     */
    Room getRoom(int id);

    /**
     * Поиск сервиса по названию
     *
     * @param id id сервиса
     * @return возвращает объект сервиса
     */
    Service getService(int id);

    /**
     * Заселение в номер
     *
     * @param guests гости
     * @param roomId id номера
     */
    void checkInRoom(List<Guest> guests, int roomId);

    /**
     * Выселение из номера
     *
     * @param guests гости
     * @param roomId id номера
     */
    void checkOutRoom(List<Guest> guests, int roomId);

    /**
     * Смена статуса номера
     *
     * @param id id номера
     * @param status новый статус
     */
    void setNewStatus(int id, Status status);

    /**
     * Смена цены номера
     *
     * @param id номер
     * @param newPrice новая цена
     */
    void setNewNumberPrice(int id, int newPrice);

    /**
     * Смена цены сервиса
     *
     * @param id сервис
     * @param newPrice новая цена
     */
    void setNewServicePrice(int id, int newPrice);

    /**
     * Добавление нового номера
     *
     * @param roomNumber численное значение номера
     * @param price цена в сутки
     * @param capacity вместимость
     * @param stars кол-во звёзд
     * @return созданный номер
     */
    Room addNewNumber(int roomNumber, int price, int capacity, int stars);

    /**
     * Добавление нового сервиса
     *
     * @param serviceName имя сервиса
     * @param price цена
     * @return созданный сервис
     */
    Service addNewService(String serviceName, int price);

    /**
     * Добавление нового гостя
     *
     * @param guestName имя гостя
     * @param email почта гостя
     * @param checkInDate дата заселения
     * @param checkOutDate дата выселения
     * @return созданный гость
     */
    Guest addNewGuest(String guestName, String email, LocalDate checkInDate,
                     LocalDate checkOutDate);

    /**
     * Оказание услуги конкретному гостю
     *
     * @param serviceId id сервиса
     * @param guestId id гостя
     * @param date дата оказания услуги
     */
    void addServiceForGuest(int serviceId, int guestId, LocalDate date);

    /**
     * Получение списка номеров
     *
     * @return список номеров
     */
    List<Room> getRooms();

    /**
     * Получение списка гостей
     *
     * @return список гостей
     */
    List<Guest> getGuests();

    List<Guest> getGustsByIds(List<Integer> guestsIds);

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
     * Получение количества свободных номеров
     *
     * @return количество свободных номеров
     */
    int getCountOfAvailableRooms();

    /**
     * Получение количества гостей
     *
     * @return количество гостей
     */
    int getCountOfGuests();

    /**
     * Получение списка свободных номеров по заданной дате
     *
     * @param date дата
     * @return список
     */
    List<Room> getAvailableRoomsOn(LocalDate date);

    /**
     * Получение суммы оплаты для определённого гостя
     *
     * @param guestId id гостя
     * @return сумма оплаты
     */
    int getPaymentOfGuest(int guestId);

    /**
     * Получение всех сервисов заданного гостя
     *
     * @param guestId id гостя
     * @return список сервисов
     */
    List<Service> getServicesOfGuest(int guestId);

    /**
     * Получение информации заданного гостя
     *
     * @param guest гость
     * @return возвращение информации
     */
    String getGuestInfo(Guest guest);

    /**
     * Получение информации заданного номера
     *
     * @param room номер
     * @return возвращение информации
     */
    String getRoomInfo(Room room);

    /**
     * Получение информации заданного сервиса
     *
     * @param service сервис
     * @return возвращение информации
     */
    String getServiceInfo(Service service);

    /**
     * Получение списка трёх последних гостей конкретного номера
     *
     * @param id id номера
     * @return список из трёх гостей
     */
    List<Guest> getLastThreeGuests(int id);

    /**
     * Получение отсортированного списка гостей
     *
     * @param guests исходный список гостей
     * @param comparator параметр сортировки
     * @return отсортированный список
     */
    List<Guest> sortGuestsBy(List<Guest> guests, Comparator<Guest> comparator);

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
     * Получение отсортированного списка номеров
     *
     * @param rooms исходный список номеров
     * @param comparator параметр сортировки
     * @return отсортированный список
     */
    List<Room> sortRoomsBy(List<Room> rooms, Comparator<Room> comparator);

    /**
     * Получение отсортированного списка свободных номеров
     *
     * @param rooms исходный список номеров
     * @param comparator параметр сортировки
     * @return отсортированный список
     */
    List<Room> sortAvailableRoomsBy(List<Room> rooms, Comparator<Room> comparator);

    void importServices(MultipartFile file);

    void importGuests(MultipartFile file);

    void importRooms(MultipartFile file);

    File exportServices();

    File exportGuests();

    File exportRooms();

    void deleteRoom(int roomId);

    void deleteGuest(int guestId);

    void deleteService(int serviceId);
}