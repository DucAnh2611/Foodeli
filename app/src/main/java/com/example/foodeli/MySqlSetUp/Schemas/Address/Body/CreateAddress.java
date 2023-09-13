package com.example.foodeli.MySqlSetUp.Schemas.Address.Body;

public class CreateAddress {

    private String address;
    private int id;

    public CreateAddress(String address, int id) {
        this.address = address;
        this.id = id;
    }
}
