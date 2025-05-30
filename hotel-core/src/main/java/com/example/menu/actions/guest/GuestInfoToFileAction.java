package com.example.menu.actions.guest;

import com.example.menu.actions.Action;
import com.example.entity.Guest;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GuestInfoToFileAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(GuestInfoToFileAction.class);

    public GuestInfoToFileAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_GUEST_ID);

        int id = scanner.nextInt();

        Guest guest = hotelManager.getGuest(id);
        if (guest == null) {
            logger.error(MessageSources.NOT_EXIST);
            return;
        }

        String info = hotelManager.getGuestInfo(guest);

        String fileName = "guestOutput.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(info);
            logger.info(MessageSources.SUCCESS_WRITE);
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_WRITE);
        }
    }
}
