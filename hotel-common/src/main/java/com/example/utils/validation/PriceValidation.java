package com.example.utils.validation;

import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidPriceException;

public class PriceValidation {

    public static int validatePrice(String priceInput) throws InvalidPriceException {
        try {
            int price = Integer.parseInt(priceInput);
            if (price < 0) {
                throw new InvalidPriceException(MessageSources.INVALID_NUMBER);
            }
            return price;
        } catch (NumberFormatException e) {
            throw new InvalidPriceException(MessageSources.INVALID_INPUT, e);
        }
    }
}
