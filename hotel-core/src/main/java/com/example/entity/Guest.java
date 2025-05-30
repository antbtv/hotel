package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "T_Guest")
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_guest_id")
    private int guestId;

    @Column(name = "c_guestname")
    private String name;

    @Column(name = "c_email")
    private String email;

    @Column(name = "c_checkindate")
    private LocalDate checkInDate;

    @Column(name = "c_checkoutdate")
    private LocalDate checkOutDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_room_id", nullable = true)
    private Room room;

    public Guest() { }

    public Guest(String name, String email, LocalDate checkInDate, LocalDate checkOutDate) {
        this.name = name;
        this.email = email;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Guest(int guestId, String name, String email, LocalDate checkInDate, LocalDate checkOutDate, Room room) {
        this.guestId = guestId;
        this.name = name;
        this.email = email;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        int roomId = 0;
        if (room != null) {
            roomId = room.getRoomId();
        }
        return "Id гостя: " + guestId + "\n" +
                "Имя: " + name + "\n" +
                "Email: " + email + "\n" +
                "Id номера: " + roomId + "\n" +
                "Период проживания: " + checkInDate + " - " +
                checkOutDate + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guest guest)) return false;
        return Objects.equals(guestId, guest.guestId) &&
                Objects.equals(name, guest.name) &&
                Objects.equals(email, guest.email) &&
                Objects.equals(checkInDate, guest.checkInDate) &&
                Objects.equals(checkOutDate, guest.checkOutDate) &&
                Objects.equals(room, guest.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId, name, email, checkInDate, checkOutDate, room);
    }
}
