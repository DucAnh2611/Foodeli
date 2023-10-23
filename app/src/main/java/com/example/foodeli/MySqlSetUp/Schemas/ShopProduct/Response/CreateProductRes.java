package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Product.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateProductRes extends ResponseApi {

    @SerializedName("p")
    private P product;

    public class P {

        @SerializedName("data")
        private Product product;

        @SerializedName("category")
        private ArrayList<Category> categories;

        @SerializedName("images")
        private ArrayList<Image> images;

        public ArrayList<Category> getCategories() {
            return categories;
        }

        public ArrayList<Image> getImages() {
            return images;
        }

        public Product getProduct() {
            return product;
        }
    }

    public static class Category {
        @SerializedName("CategoryID")
        private int id;

        @SerializedName("CategoryName")
        private String name;

        @SerializedName("CategoryImage")
        private String image;

        @SerializedName("CategoryParentId")
        private int parentId;

        public Category(int id, String name, String image, int parentId) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public int getParentId() {
            return parentId;
        }
    }

    public static class Image {

        @SerializedName("id")
        private int iid;

        @SerializedName("base64")
        private String base64;

        public Image(int iid, String base64) {
            this.iid = iid;
            this.base64 = base64;
        }
        public int getIid() {
            return iid;
        }

        public String getBase64() {
            return base64;
        }
    }

    public P getProduct() {
        return product;
    }
}
