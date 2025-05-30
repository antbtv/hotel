package com.example.menu.actions.service;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidPriceException;
import com.example.utils.validation.PriceValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class ChangePriceOfServiceAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(ChangePriceOfServiceAction.class);

    public ChangePriceOfServiceAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {

        System.out.println(MessageSources.ENTER_SERVICE_ID);
        int id = scanner.nextInt();

        int price = 0;
        boolean validPrice = false;

        while (!validPrice) {
            System.out.println(MessageSources.ENTER_PRICE);
            String inputPrice = scanner.next();
            scanner.nextLine();
            price = Integer.parseInt(inputPrice);
            try {
                price = PriceValidation.validatePrice(inputPrice);
                validPrice = true;
            } catch (InvalidPriceException e) {
                logger.error(MessageSources.INVALID_NUMBER);
            }
        }

        hotelManager.setNewServicePrice(id, price);
    }
}
