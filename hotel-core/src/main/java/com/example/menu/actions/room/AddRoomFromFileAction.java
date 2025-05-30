package com.example.menu.actions.room;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidPriceException;
import com.example.utils.validation.PriceValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AddRoomFromFileAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(AddRoomFromFileAction.class);

    public AddRoomFromFileAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_FILE);
        String fileName = scanner.next();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            int number = Integer.parseInt(line);
            if (number < 1) {
                logger.error(MessageSources.INVALID_NUMBER);
                return;
            }

            line = reader.readLine();
            int price = Integer.parseInt(line);
            try {
                price = PriceValidation.validatePrice(line);
            } catch (InvalidPriceException e) {
                logger.error(MessageSources.INVALID_NUMBER);
            }

            line = reader.readLine();
            int capacity = Integer.parseInt(line);
            if (capacity < 0 || capacity > 10) {
                logger.error(MessageSources.INVALID_INPUT);
                return;
            }

            line = reader.readLine();
            int stars = Integer.parseInt(line);
            if (stars < 0 || stars > 10) {
                logger.error(MessageSources.INVALID_INPUT);
                return;
            }

            hotelManager.addNewNumber(number, price, capacity, stars);
        } catch (FileNotFoundException e) {
            logger.error(MessageSources.FILE_NOT_FOUND);
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_FILE_READ);
        } catch (NumberFormatException e) {
            logger.error(MessageSources.FAILURE_CONVERSION);
        }
    }
}
