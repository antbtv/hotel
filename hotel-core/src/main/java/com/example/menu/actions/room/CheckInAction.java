package com.example.menu.actions.room;

import com.example.menu.actions.Action;
import com.example.entity.Guest;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CheckInAction implements Action {

    private final Scanner scanner;

    public CheckInAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_GUEST_ID);
        int guestId = scanner.nextInt();

        List<Guest> guests = new ArrayList<>();
        Guest guest = hotelManager.getGuest(guestId);
        guests.add(guest);

        System.out.println("Введите id номера:");
        int roomId = scanner.nextInt();

        hotelManager.checkInRoom(guests, roomId);
    }
}
