package com.example.menu.actions.service;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidDateException;
import com.example.utils.validation.DateValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Scanner;

public class AddServiceForGuestAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(AddServiceAction.class);

    public AddServiceForGuestAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {

        System.out.println(MessageSources.ENTER_SERVICE_ID);
        int serviceId = scanner.nextInt();

        System.out.println(MessageSources.ENTER_GUEST_ID);
        int guestId = scanner.nextInt();

        LocalDate serviceDate = null;
        while (serviceDate == null) {
            System.out.println(MessageSources.ENTER_DATE);
            String serviceDateInput = scanner.next();
            scanner.nextLine();
            try {
                serviceDate = DateValidation.validateAndParse(serviceDateInput);
            } catch (InvalidDateException e) {
                logger.error(MessageSources.INVALID_DATE);
            }
        }

        hotelManager.addServiceForGuest(serviceId, guestId, serviceDate);
    }
}
