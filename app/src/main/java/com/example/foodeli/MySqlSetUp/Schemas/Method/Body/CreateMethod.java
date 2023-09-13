package com.example.foodeli.MySqlSetUp.Schemas.Method.Body;

public class CreateMethod {
    private int u_id, m_id;
    private String number = "", expired = "", desc ="";

    public CreateMethod(int uid, int mid, String number, String expired, String desc) {
        this.u_id = uid;
        this.m_id = mid;
        this.number = number;
        this.expired = expired;
        this.desc = desc;
    }
}
