package com.example.menu.actions.room;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidPriceException;
import com.example.utils.validation.PriceValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class AddRoomAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(AddRoomAction.class);

    public AddRoomAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_ROOM);
        int roomNumber = scanner.nextInt();

        System.out.println(MessageSources.ENTER_PRICE);
        String inputPrice = scanner.next();
        int price = Integer.parseInt(inputPrice);
        try {
            price = PriceValidation.validatePrice(inputPrice);
        } catch (InvalidPriceException e) {
            logger.error(MessageSources.INVALID_NUMBER);
        }

        System.out.println("Введите вместимость");
        int capacity = scanner.nextInt();
        if (capacity < 0 || capacity > 10) {
            logger.error(MessageSources.INVALID_INPUT);
            return;
        }

        System.out.println("Введите количество звёзд");
        int stars = scanner.nextInt();
        if (stars < 0 || stars > 10) {
            logger.error(MessageSources.INVALID_INPUT);
            return;
        }

        hotelManager.addNewNumber(roomNumber, price, capacity, stars);
    }
}
