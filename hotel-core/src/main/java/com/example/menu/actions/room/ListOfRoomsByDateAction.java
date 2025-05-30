package com.example.menu.actions.room;

import com.example.entity.Room;
import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidDateException;
import com.example.utils.validation.DateValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ListOfRoomsByDateAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(ListOfRoomsByDateAction.class);

    public ListOfRoomsByDateAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        LocalDate date = null;
        while (date == null) {
            System.out.println("Введите дату (гггг-мм-дд):");
            String dateInput = scanner.next();
            scanner.nextLine();
            try {
                date = DateValidation.validateAndParse(dateInput);
            } catch (InvalidDateException e) {
                logger.error(MessageSources.INVALID_DATE);
            }
        }

        List<Room> rooms = hotelManager.getAvailableRoomsOn(date);
        System.out.println("Свободные номера на заданную дату:");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}
