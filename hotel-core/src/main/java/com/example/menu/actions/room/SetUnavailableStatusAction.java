package com.example.menu.actions.room;

import com.example.enums.Status;
import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;

import java.util.Scanner;

public class SetUnavailableStatusAction implements Action {

    private final Scanner scanner;

    public SetUnavailableStatusAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_ROOM_ID);
        int id = scanner.nextInt();

        hotelManager.setNewStatus(id, Status.UNAVAILABLE);
    }
}
