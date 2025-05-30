package com.example.menu.actions.guest;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;

public class QuantityOfGuestsAction implements Action {

    public QuantityOfGuestsAction() { }

    @Override
    public void execute(HotelManager hotelManager) {
        int count = hotelManager.getCountOfGuests();
        System.out.println("Количество гостей: " + count);
    }
}
