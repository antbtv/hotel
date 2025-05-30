package com.example.dto;

import java.time.LocalDate;

public class CreateProvidedServiceDTO {

    private int serviceId;
    private LocalDate serviceDate;

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
