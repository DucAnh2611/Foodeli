package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Order;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderTrackRes extends ResponseApi {

    @SerializedName("order")
    private OrderInfo order;

    public static class OrderInfo {
        @SerializedName("info")
        private OrderWithValue info;

        @SerializedName("timeline")
        private ArrayList<OrderTimeLine> orderTimeLines;

        public OrderWithValue getInfo() {
            return info;
        }

        public ArrayList<OrderTimeLine> getOrderTimeLines() {
            return orderTimeLines;
        }
    }

    public static class OrderItems{

        @SerializedName("ProductID")
        private int pid;
        @SerializedName("ProductName")
        private String pname;
        @SerializedName("ProductPrice")
        private float price;
        @SerializedName("ProductQuantity")
        private float quantity;
        @SerializedName("ProductUnit")
        private String unit;
        @SerializedName("ProdctImage")
        private ProductImage image;

        public int getPid() {
            return pid;
        }

        public String getPname() {
            return pname;
        }

        public float getPrice() {
            return price;
        }

        public float getQuantity() {
            return quantity;
        }

        public String getUnit() {
            return unit;
        }

        public ProductImage getImage() {
            return image;
        }
    }

    public static class ProductImage {
        @SerializedName("id") private int productImageId;
        @SerializedName("base64") private String base64Image;

        public int getProductImageId() {
            return productImageId;
        }

        public String getBase64Image() {
            return base64Image;
        }
    }

    public static class OrderTimeLine {
        @SerializedName("StateID") private int stateTimelineId;
        @SerializedName("UpdateAt") private String updateAt;

        public int getStateTimelineId() {
            return stateTimelineId;
        }

        public String getUpdateAt() {
            return updateAt;
        }
    }

    public static class OrderWithValue  extends Order {
        @SerializedName("OrderEstimate")
        private String estimate;
        @SerializedName("OrderSubTotal")
        private double subtotal;

        public String getEstimate() {
            return estimate;
        }

        public double getSubtotal() {
            return subtotal;
        }
    }

    public OrderInfo getOrder() {
        return order;
    }
}
