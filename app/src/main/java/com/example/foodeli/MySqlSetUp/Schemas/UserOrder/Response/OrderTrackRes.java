package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Order;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderTrackRes extends ResponseApi {

    @SerializedName("order")
    private OrderInfo order;

    public static class OrderInfo implements Serializable {
        @SerializedName("info")
        private OrderWithValue info;

        @SerializedName("timeline")
        private ArrayList<OrderTimeLine> orderTimeLines;

        @SerializedName("payment")
        private PaymentMethod paymentMethod;

        public OrderWithValue getInfo() {
            return info;
        }

        public ArrayList<OrderTimeLine> getOrderTimeLines() {
            return orderTimeLines;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }
    }

    public static class OrderTimeLine implements Serializable {
        @SerializedName("StateID") private int stateTimelineId;
        @SerializedName("UpdateAt") private String updateAt;

        public int getStateTimelineId() {
            return stateTimelineId;
        }

        public String getUpdateAt() {
            return updateAt;
        }
    }

    public static class OrderWithValue extends Order implements Serializable {
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

    public static class PaymentMethod implements Serializable {
        @SerializedName("UserMethodNumber") private String number;
        @SerializedName("MethodType") private String methodType;

        public String getNumber() {
            return number;
        }

        public String getMethodType() {
            return methodType;
        }
    }

    public OrderInfo getOrder() {
        return order;
    }
}
