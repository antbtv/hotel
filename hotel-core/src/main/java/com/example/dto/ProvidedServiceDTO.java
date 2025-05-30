package com.example.dto;

import java.time.LocalDate;

public class ProvidedServiceDTO {

    private int id;
    private int guestId;
    private int serviceId;
    private LocalDate serviceDate;

    public ProvidedServiceDTO(int id, int guestId, int serviceId, LocalDate serviceDate) {
        this.id = id;
        this.guestId = guestId;
        this.serviceId = serviceId;
        this.serviceDate = serviceDate;
    }

    public ProvidedServiceDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }
}
