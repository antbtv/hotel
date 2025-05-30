package com.example.entity;

import com.example.enums.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Entity
@Table(name = "T_Room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_room_id")
    private int roomId;

    @Column(name = "c_number")
    private int number;

    @Enumerated(EnumType.STRING)
    @Column(name = "c_status")
    private Status status;

    @Column(name = "c_price")
    private int price;

    @Column(name = "c_capacity")
    private int capacity;

    @Column(name = "c_stars")
    private int stars;

    @OneToMany(mappedBy = "room")
    private List<Guest> guests;

    @Transient
    private static int maxHistory;

    @Transient
    private static int maxPrice;

    public Room() { }

    public Room(int number, int price, int capacity, int stars) {
        this.number = number;
        this.status = Status.AVAILABLE;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.guests = new ArrayList<>();
    }

    public Room(int roomId, int number, Status status, int price, int capacity, int stars, List<Guest> guests) {
        this.roomId = roomId;
        this.number = number;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.guests = guests;
    }

    @Value("${room.history.max.records}")
    public void setMaxHistory(int maxHistory) {
        Room.maxHistory = maxHistory;
    }

    @Value("${room.max.price}")
    public void setMaxPrice(int maxPrice) {
        Room.maxPrice = maxPrice;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    public int getMaxHistory() {
        return maxHistory;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isAvailableOn(LocalDate date) {
        if (guests == null || guests.isEmpty()) {
            return true;
        }
        LocalDate checkOutDate = guests.get(0).getCheckOutDate();
        return checkOutDate == null || checkOutDate.isBefore(date);
    }

    @Override
    public String toString() {
        return "Id номера: " + roomId + "\n" +
                "Номер: " + getNumber() + "\n" +
                "Статус: " + getStatus() + "\n" +
                "Стоимость: " + getPrice() + "\n" +
                "Максимальное кол-во гостей: " + getCapacity() + "\n" +
                "Кол-во звёзд: " + getStars() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return Objects.equals(roomId, room.getRoomId()) &&
                Objects.equals(number, room.getNumber()) &&
                Objects.equals(status, room.getStatus()) &&
                Objects.equals(price, room.getPrice()) &&
                Objects.equals(capacity, room.getCapacity()) &&
                Objects.equals(stars, room.getStars());
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, number, status, price, capacity, stars);
    }
}