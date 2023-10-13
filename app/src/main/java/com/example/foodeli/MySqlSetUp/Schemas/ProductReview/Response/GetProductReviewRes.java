package com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetProductReviewRes extends ResponseApi {

    @SerializedName("review")
    private ArrayList<ReviewWithImage> reviews;

    public ArrayList<ReviewWithImage> getReviews() {
        return reviews;
    }

    public static class ReviewWithImage {
        @SerializedName("UserFullName")
        private String fullname;

        @SerializedName("UserAvatar")
        private String avatar;

        @SerializedName("UserEmail")
        private String email;

        @SerializedName("ReviewId")
        private int rid;

        @SerializedName("UserId")
        private int uid;

        @SerializedName("ProductId")
        private int pid;

        @SerializedName("OrderId")
        private int oid;

        @SerializedName("ReviewTitle")
        private String title;

        @SerializedName("ReviewDesc")
        private String desc;

        @SerializedName("ReviewCreateAt")
        private String create;

        @SerializedName("ReviewRating")
        private int rate;

        @SerializedName("image")
        private ArrayList<ImageReview> images;

        public int getUid() {
            return uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getCreate() {
            return create;
        }

        public int getPid() {
            return pid;
        }

        public int getRate() {
            return rate;
        }

        public int getOid() {
            return oid;
        }

        public int getRid() {
            return rid;
        }

        public String getDesc() {
            return desc;
        }

        public String getEmail() {
            return email;
        }

        public String getFullname() {
            return fullname;
        }

        public String getTitle() {
            return title;
        }

        public ArrayList<ImageReview> getImages() {
            return images;
        }
    }

    public static class ImageReview {
        @SerializedName("ReviewImageId")
        private int riid;

        @SerializedName("Image")
        private String image;

        public String getImage() {
            return image;
        }

        public int getRiid() {
            return riid;
        }
    }
}
