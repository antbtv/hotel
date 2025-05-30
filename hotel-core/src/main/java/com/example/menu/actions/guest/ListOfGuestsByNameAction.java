package com.example.menu.actions.guest;

import com.example.entity.Guest;
import com.example.menu.actions.Action;
import com.example.enums.SortOption;
import com.example.service.HotelManager;
import com.example.comparator.ComparatorFactory;

import java.util.List;

public class ListOfGuestsByNameAction implements Action {

    public ListOfGuestsByNameAction() { }

    @Override
    public void execute(HotelManager hotelManager) {
        List<Guest> guests = hotelManager.sortGuestsBy(hotelManager.getGuests(),
                ComparatorFactory.getGuestComparator(SortOption.NAME));

        System.out.println("Список гостей и их номеров:");
        for (Guest guest : guests) {
            System.out.println(guest);
        }
    }
}
