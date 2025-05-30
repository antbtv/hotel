package com.example.service;

import com.example.entity.Guest;
import com.example.entity.Room;
import com.example.enums.Status;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Интерфейс для управления номерами
 */
public interface RoomService {

    /**
     * Заселение в номер
     *
     * @param room номер
     * @param guests список гостей
     */
    void checkIn(Room room, List<Guest> guests);

    /**
     * Выселение из номера
     *
     * @param room номер
     * @param guests список гостей
     */
    void checkOut(Room room, List<Guest> guests);

    /**
     * Проверка возможности изменения статуса
     *
     * @return возможность изменить статус
     */
    boolean canChangeStatus();

    /**
     * Поиск номера по числовому значению
     *
     * @param id id номера
     * @return возвращает объект номера
     */
    Room getRoom(int id);

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
    void setNewRoomPrice(int id, int newPrice);

    /**
     * Добавление нового номера
     *
     * @param roomNumber численное значение номера
     * @param price цена в сутки
     * @param capacity вместимость
     * @param stars кол-во звёзд
     * @return созданный номер
     */
    Room addNewRoom(int roomNumber, int price, int capacity, int stars);

    /**
     * Получение списка номеров
     *
     * @return список номеров
     */
    List<Room> getRooms();

    /**
     * Получение количества свободных номеров
     *
     * @return количество свободных номеров
     */
    int getCountOfAvailableRooms();

    /**
     * Получение списка свободных номеров по заданной дате
     *
     * @param date дата
     * @return список
     */
    List<Room> getAvailableRoomsOn(LocalDate date);

    /**
     * Получение информации заданного номера
     *
     * @param room номер
     * @return возвращение информации
     */
    String getRoomInfo(Room room);

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

    /**
     * Импорт номеров из файла
     *
     * @param file файл
     */
    void importRooms(MultipartFile file);

    /**
     * Экспорт номеров в файл
     *
     * @return файл со списком номеров
     */
    File exportRooms();

    /**
     * Удаление комнаты по id
     *
     * @param roomId id комнаты
     */
    void deleteRoom(int roomId);
}
