package com.example.foodeli.MySqlSetUp.Schemas.Method.Response;

import com.example.foodeli.MySqlSetUp.Response;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.CreateMethod;
import com.google.gson.annotations.SerializedName;

public class CreateMethodRes extends Response {

    @SerializedName("method")
    private CreateMethod method;

    public class CreatedMethod {
        @SerializedName("UserMethodId")
        private int id;
        @SerializedName("UserId")
        private int uid;
        @SerializedName("MethodId")
        private int mid;
        @SerializedName("Number")
        private String number;
        @SerializedName("ExpiredAt")
        private String expired;
        @SerializedName("Description")
        private String desc;
        @SerializedName("Deleted")
        private int delete;
    }

    public CreateMethod getMethod() {return this.method;}


}
