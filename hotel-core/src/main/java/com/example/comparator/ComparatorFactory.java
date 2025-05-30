package com.example.comparator;

import com.example.entity.Guest;
import com.example.entity.ProvidedService;
import com.example.entity.Room;
import com.example.entity.Service;
import com.example.enums.SortOption;

import java.util.Comparator;

public class ComparatorFactory {

    public static Comparator<Service> getServiceComparator(SortOption choice) {
        return switch (choice) {
            case PRICE -> Comparator.comparingInt(Service::getPrice);
            case NAME -> Comparator.comparing(Service::getName);
            default -> throw new IllegalArgumentException();
        };
    }

    public static Comparator<ProvidedService> getProvidedServicesComparator(SortOption choice) {
        if (choice == SortOption.DATE) {
            return Comparator.comparing(ProvidedService::getDate);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Comparator<Guest> getGuestComparator(SortOption choice) {
        return switch (choice) {
            case DATE -> Comparator.comparing(Guest::getCheckOutDate);
            case NAME -> Comparator.comparing(Guest::getName);
            default -> throw new IllegalArgumentException();
        };
    }

    public static Comparator<Room> getRoomComparator(SortOption choice) {
        return switch (choice) {
            case PRICE -> Comparator.comparing(Room::getPrice);
            case STARS -> Comparator.comparing(Room::getStars);
            case CAPACITY -> Comparator.comparing(Room::getCapacity);
            default -> throw new IllegalArgumentException();
        };
    }
}
