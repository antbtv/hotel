package com.example.utils.validation;

import com.example.utils.MessageSources;
import com.example.utils.exception.InvalidDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidation {

    public static LocalDate validateAndParse(String dateInput) throws InvalidDateException {
        try {
            LocalDate parsedDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
            if (parsedDate.getYear() < 1900 || parsedDate.getYear() > 2025) {
                throw new InvalidDateException(MessageSources.INVALID_YEAR);
            }
            return parsedDate;
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(MessageSources.INVALID_DATE, e);
        }
    }
}

