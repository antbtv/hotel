package com.example.service;

import com.example.entity.Guest;
import com.example.entity.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Интерфейс для управления гостями
 */
public interface GuestService {

    /**
     * Расчёт суммы оплаты гостя за проживание
     *
     * @param guestId id гостя
     * @return сумма оплаты за время проживания
     */
    int calculateRoomPrice(int guestId);

    /**
     * Поиск гостя по имени
     *
     * @param id id гостя
     * @return возвращает объект гостя
     */
    Guest getGuest(int id);

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
     * Получение списка гостей
     *
     * @return список гостей
     */
    List<Guest> getGuests();

    /**
     * Получение списка гостей по списку id
     *
     * @param guestsIds список id
     * @return список гостей
     */
    List<Guest> getGustsByIds(List<Integer> guestsIds);

    /**
     * Получение количества гостей
     *
     * @return количество гостей
     */
    int getCountOfGuests();

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
     * Импорт гостей из файла
     *
     * @param file файл
     */
    void importGuests(MultipartFile file);

    /**
     * Экспорт гостей в файл
     *
     * @return файл со списком гостей
     */
    File exportGuests();

    /**
     * Удаление гостя по id
     *
     * @param guestId id гостя
     */
    void deleteGuest(int guestId);
}
