package com.example.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Objects;

@Component
@Entity
@Table(name = "T_Service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_service_id")
    private int serviceId;

    @Column(name = "c_servicename")
    private String name;

    @Column(name = "c_price")
    private int price;

    @Transient
    private static int maxPrice;

    public Service() { }

    public Service(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Service(int serviceId, String name, int price) {
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
    }

    @Value("${service.max.price}")
    public void setMaxPrice(int maxPrice) {
        Service.maxPrice = maxPrice;
    }

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

    @Override
    public String toString() {
        return "Id сервиса: " + serviceId + "\n" +
                "Название услуги: " + getName() + "\n" +
                "Стоимость: " + getPrice() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service service)) return false;
        return Objects.equals(serviceId, service.getServiceId()) &&
                Objects.equals(name, service.getName()) &&
                Objects.equals(price, service.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, name, price);
    }
}
