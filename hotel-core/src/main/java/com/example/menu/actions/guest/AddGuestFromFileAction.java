package com.example.menu.actions.guest;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidDateException;
import com.example.utils.validation.DateValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class AddGuestFromFileAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(AddGuestFromFileAction.class);

    public AddGuestFromFileAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_FILE);
        String fileName = scanner.next();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String name = reader.readLine();
            String email = reader.readLine();

            String checkInInput = reader.readLine();
            LocalDate checkInDate = DateValidation.validateAndParse(checkInInput);

            String checkOutInput = reader.readLine();
            LocalDate checkOutDate = DateValidation.validateAndParse(checkOutInput);

            if (checkInDate.isAfter(checkOutDate)) {
                System.out.println(MessageSources.INVALID_CHECKOUT);
                return;
            }

            hotelManager.addNewGuest(name, email, checkInDate, checkOutDate);
        } catch (FileNotFoundException e) {
            logger.error(MessageSources.FILE_NOT_FOUND);
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_FILE_READ);
        } catch (InvalidDateException e) {
            logger.error(MessageSources.INVALID_DATE);
        } catch (NumberFormatException e) {
            logger.error(MessageSources.FAILURE_CONVERSION);
        }
    }
}
