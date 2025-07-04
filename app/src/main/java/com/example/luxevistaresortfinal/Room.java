package com.example.luxevistaresortfinal;

public class Room {
    int id;
    String roomName;
    String description;
    double price;

    public Room(int id, String roomName, String description, double price){
        this.id = id;
        this.roomName = roomName;
        this.description = description;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

