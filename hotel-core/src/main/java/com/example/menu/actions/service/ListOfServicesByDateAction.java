package com.example.menu.actions.service;

import com.example.entity.ProvidedService;
import com.example.menu.actions.Action;
import com.example.enums.SortOption;
import com.example.service.HotelManager;
import com.example.comparator.ComparatorFactory;

import java.util.List;

public class ListOfServicesByDateAction implements Action {

    public ListOfServicesByDateAction() { }

    @Override
    public void execute(HotelManager hotelManager) {
        List<ProvidedService> providedServices = hotelManager.sortProvidedServicesBy(
                hotelManager.getProvidedServices(),
                ComparatorFactory.getProvidedServicesComparator(SortOption.DATE));

        System.out.println("Список сервисов:");
        for (ProvidedService providedService : providedServices) {
            int serviceId = providedService.getService().getServiceId();
            System.out.println("Дата: " + providedService.getDate());
            System.out.println(hotelManager.getService(serviceId));
        }
    }
}
