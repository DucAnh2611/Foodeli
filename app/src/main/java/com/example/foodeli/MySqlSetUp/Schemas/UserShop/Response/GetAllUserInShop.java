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
        public UserInShop(User user, String role, String update) {
            super(user.getId(), user.getName(), user.getPassword(), user.getAvatar(), user.getPhone(), user.getEmail(), user.getBirthday(), user.getRegisAt(), user.getModified());
            this.role = role;
            this.update = update;
        }

        @SerializedName("PermissionType")
        private String role;

        @SerializedName("update")
        private String update;


        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }
    }
}
