package com.example.statemanager;

import com.example.entity.Hotel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class StateManager {

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(
            new JavaTimeModule());
    private static final String  FILE_NAME = "hotel.json";

    private static final Logger logger = LogManager.getLogger(StateManager.class);

    public static void saveHotel(Hotel hotel) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), hotel);
        } catch (IOException e) {
            logger.error("Ошибка при записи состояния");
        }
    }

    public static Hotel loadHotel() {
        try {
            return mapper.readValue(new File(FILE_NAME), Hotel.class);
        } catch (IOException e) {
            logger.error("Ошибка при загрузки состояния");
            return new Hotel();
        }
    }
}
