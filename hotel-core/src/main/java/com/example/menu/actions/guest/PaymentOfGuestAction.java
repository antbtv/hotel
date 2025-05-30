package com.example.menu.actions.guest;

import com.example.menu.actions.Action;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;

import java.util.Scanner;

public class PaymentOfGuestAction implements Action {

    private final Scanner scanner;

    public PaymentOfGuestAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_GUEST_ID);
        int id = scanner.nextInt();

        int sum = hotelManager.getPaymentOfGuest(id);

        System.out.println("Сумма оплаты постояльца: " + sum);
    }
}
