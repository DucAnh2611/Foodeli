package com.example.foodeli.Activities.MethodCheckout;

import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.CreateMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;

public interface OnSelectMethodUpdate {
    void onCreatePayment(int mid, String type, String number, String expire);
}
