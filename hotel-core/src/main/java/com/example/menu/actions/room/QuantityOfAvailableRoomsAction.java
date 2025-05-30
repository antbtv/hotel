package com.example.menu.actions.room;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;

public class QuantityOfAvailableRoomsAction implements Action {

    public QuantityOfAvailableRoomsAction() { }

    @Override
    public void execute(HotelManager hotelManager) {
        int count = hotelManager.getCountOfAvailableRooms();
        System.out.println("Количество номеров: " + count);
    }
}
