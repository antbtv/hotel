package com.example.menu.actions.guest;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidDateException;
import com.example.utils.validation.DateValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Scanner;

public class AddGuestAction implements Action {

    private final Scanner scanner;

    private static final Logger logger = LogManager.getLogger(AddGuestAction.class);

    public AddGuestAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_NAME);
        String name = scanner.next();
        scanner.nextLine();

        System.out.println("Введите его email");
        String email = scanner.next();
        scanner.nextLine();

        LocalDate checkInDate = null;
        while (checkInDate == null) {
            System.out.println("Введите дату заселения (гггг-мм-дд):");
            String checkInInput = scanner.next();
            scanner.nextLine();
            try {
                checkInDate = DateValidation.validateAndParse(checkInInput);
            } catch (InvalidDateException e) {
                logger.error(MessageSources.INVALID_DATE);
            }
        }

        LocalDate checkOutDate = null;
        while (checkOutDate == null) {
            System.out.println("Введите дату выселения (гггг-мм-дд):");
            String checkOutInput = scanner.next();
            scanner.nextLine();
            try {
                checkOutDate = DateValidation.validateAndParse(checkOutInput);
            } catch (InvalidDateException e) {
                logger.error(MessageSources.INVALID_DATE);
            }
        }

        if (checkInDate.isAfter(checkOutDate)) {
            System.out.println(MessageSources.INVALID_CHECKOUT);
            return;
        }

        hotelManager.addNewGuest(name, email, checkInDate, checkOutDate);
    }
}
