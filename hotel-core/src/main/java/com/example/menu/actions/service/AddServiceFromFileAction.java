package com.example.menu.actions.service;

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

public class AddServiceFromFileAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(AddServiceFromFileAction.class);

    public AddServiceFromFileAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_FILE);
        String fileName = scanner.next();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String serviceName = reader.readLine();

            String line = reader.readLine();
            int price = Integer.parseInt(line);
            try {
                price = PriceValidation.validatePrice(line);
            } catch (InvalidPriceException e) {
                logger.error(MessageSources.INVALID_NUMBER);
            }

            hotelManager.addNewService(serviceName, price);
        } catch (FileNotFoundException e) {
            System.out.println(MessageSources.FILE_NOT_FOUND);
        } catch (IOException e) {
            System.out.println(MessageSources.FAILURE_FILE_READ);
        }
    }
}
