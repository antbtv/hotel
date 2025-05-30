package com.example.menu.actions;

import com.example.statemanager.StateManager;
import com.example.service.HotelManager;

public class ExitAction implements Action {

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println("Выход из программы");
        StateManager.saveHotel(hotelManager.getHotel());
        System.exit(0);
    }
}
