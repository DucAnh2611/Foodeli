package com.example.foodeli.MySqlSetUp.Schemas.General.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryRes extends ResponseApi {

    @SerializedName("category")
    private ArrayList<Category> categories;

    public ArrayList<Category> getCategories() {
        return categories;
    }
}
