package com.example.dao;

import com.example.entity.Guest;

import java.util.List;

public interface GuestDAO extends GenericDAO<Guest> {

    /**
     * Получение трёх последних гостей определённого номера
     *
     * @param roomId id номера
     * @return список из трёх гостей
     */
    List<Guest> getLastThreeGuests(int roomId);

    /**
     * Получение гостей по списку id
     *
     * @param guestIds список id
     * @return список гостей
     */
    List<Guest> findByIds(List<Integer> guestIds);
}
