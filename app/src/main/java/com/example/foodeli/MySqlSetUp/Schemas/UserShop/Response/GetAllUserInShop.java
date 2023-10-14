package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllUserInShop {

    @SerializedName("users")
    private ArrayList<UserInShop> users;

    public ArrayList<UserInShop> getUsers() {
        return users;
    }

    public static class UserInShop extends User {
        public UserInShop(String name, String phone, String email, String birthday, String regisAt, String modified) {
            super(name, phone, email, birthday, regisAt, modified);
        }

        public UserInShop(String phone, String email, String password) {
            super(phone, email, password);
        }

        @SerializedName("PermissionType")
        private String role;

        public String getRole() {
            return role;
        }
    }
}
