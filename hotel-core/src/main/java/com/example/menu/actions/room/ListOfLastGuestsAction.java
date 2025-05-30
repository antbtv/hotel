package com.example.menu.actions.room;

import com.example.entity.Guest;
import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;

import java.util.List;
import java.util.Scanner;

public class ListOfLastGuestsAction implements Action {

    private final Scanner scanner;

    public ListOfLastGuestsAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_ROOM_ID);
        int id = scanner.nextInt();

        List<Guest> guests = hotelManager.getLastThreeGuests(id);

        for (Guest guest : guests) {
            System.out.println(guest);
        }
    }
}
