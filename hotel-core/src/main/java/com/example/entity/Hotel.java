package com.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Hotel {

    private List<Room> rooms;
    private List<Service> services;
    private List<Guest> guests;

    public Hotel() {
        rooms = new ArrayList<>();
        services = new ArrayList<>();
        guests = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addService(Service service) {
        services.add(service);
    }

    public void addGuest(Guest guest) {
        guests.add(guest);
    }

    public Room getRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getNumber() == roomNumber) {
                return room;
            }
        }
        System.out.println("Номер '" + roomNumber + "' не найден");
        return null;
    }

    public Service getService(String serviceName) {
        for (Service service : services) {
            if (service.getName().equals(serviceName)) {
                return service;
            }
        }
        System.out.println("Сервис '" + serviceName + "' не найден");
        return null;
    }

    public Guest getGuest(String guestName) {
        for (Guest guest : guests) {
            if (guest.getName().equals(guestName)) {
                return guest;
            }
        }
        System.out.println("Гость '" + guestName + "' не найден");
        return null;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Service> getServices() {
        return services;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    @Override
    public String toString() {
        return "Отель{" +
                "Номера=" + rooms +
                ", Гости=" + guests +
                ", Сервисы=" + services +
                "}";
    }
}
