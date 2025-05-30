package com.example.dto;

import java.time.LocalDate;

public class CreateGuestDTO {

    private String name;
    private String email;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public CreateGuestDTO() {
    }

    public CreateGuestDTO(String name, String email, LocalDate checkInDate, LocalDate checkOutDate) {
        this.name = name;
        this.email = email;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
