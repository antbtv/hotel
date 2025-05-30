package com.example.menu.actions.room;

import com.example.entity.Guest;
import com.example.entity.Room;
import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;

import java.util.List;
import java.util.Scanner;

public class CheckOutAction implements Action {

    private final Scanner scanner;

    public CheckOutAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_ROOM_ID);
        int roomId = scanner.nextInt();

        Room room = hotelManager.getRoom(roomId);
        List<Guest> guests = room.getGuests();

        hotelManager.checkOutRoom(guests, roomId);
    }
}
