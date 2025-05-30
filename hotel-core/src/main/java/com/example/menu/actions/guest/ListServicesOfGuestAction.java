package com.example.menu.actions.guest;

import com.example.entity.Service;
import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;

import java.util.List;
import java.util.Scanner;

public class ListServicesOfGuestAction implements Action {

    private final Scanner scanner;

    public ListServicesOfGuestAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_GUEST_ID);
        int id = scanner.nextInt();

        List<Service> services = hotelManager.getServicesOfGuest(id);

        for (Service service : services) {
            System.out.println(service);
        }
    }
}
