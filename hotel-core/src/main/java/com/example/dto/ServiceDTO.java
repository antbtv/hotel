package com.example.dto;

public class ServiceDTO {

    private int serviceId;
    private String name;
    private int price;

    public ServiceDTO(int serviceId, String name, int price) {
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
    }

    public ServiceDTO() { }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
