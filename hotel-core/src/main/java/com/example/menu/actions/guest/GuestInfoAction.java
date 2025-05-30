package com.example.menu.actions.guest;

import com.example.menu.actions.Action;
import com.example.entity.Guest;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class GuestInfoAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(GuestInfoAction.class);

    public GuestInfoAction(Scanner scanner) {
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
        System.out.println(info);
    }
}
