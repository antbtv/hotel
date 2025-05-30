package com.example.menu.actions.service;

import com.example.entity.Service;
import com.example.menu.actions.Action;
import com.example.enums.SortOption;
import com.example.service.HotelManager;
import com.example.comparator.ComparatorFactory;

import java.util.List;

public class ListOfServicesByPriceAction implements Action {

    public ListOfServicesByPriceAction() { }

    @Override
    public void execute(HotelManager hotelManager) {
        List<Service> services = hotelManager.sortServicesBy(
                hotelManager.getServices(), ComparatorFactory.getServiceComparator(SortOption.PRICE));

        System.out.println("Список сервисов:");
        for (Service service : services) {
            System.out.println(service);
        }
    }
}
