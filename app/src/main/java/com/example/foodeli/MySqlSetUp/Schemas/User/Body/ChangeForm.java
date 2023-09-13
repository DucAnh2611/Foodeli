package com.example.foodeli.MySqlSetUp.Schemas.User.Body;

public class ChangeForm {
    private String field, data;

    public ChangeForm(String field, String data) {
        this.field = field;
        this.data = data;
    }
}
