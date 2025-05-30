package com.example.dto;

public class CreateRoomDTO {

    private int number;
    private int price;
    private int capacity;
    private int stars;

    public CreateRoomDTO() {
    }

    public CreateRoomDTO(int number, int price, int capacity, int stars) {
        this.number = number;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
}
