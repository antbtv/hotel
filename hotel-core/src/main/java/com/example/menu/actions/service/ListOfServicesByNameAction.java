package com.example.menu.actions.service;

import com.example.comparator.ComparatorFactory;
import com.example.entity.Service;
import com.example.enums.SortOption;
import com.example.menu.actions.Action;
import com.example.service.HotelManager;

import java.util.List;

public class ListOfServicesByNameAction implements Action {

    @Override
    public void execute(HotelManager hotelManager) {
        List<Service> services = hotelManager.sortServicesBy(
                hotelManager.getServices(), ComparatorFactory.getServiceComparator(SortOption.NAME));

        System.out.println("Список сервисов:");
        for (Service service : services) {
            System.out.println(service);
        }
    }
}
