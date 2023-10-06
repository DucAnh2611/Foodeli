package com.example.foodeli.Activities.SelectPayment;

import com.example.foodeli.R;

public class ConvertIconMethodIcon {

    public int convertTypeToIcon(String type){
        switch (type.toLowerCase()) {
            case "cash":
                return R.drawable.cash;
            case "visa":
                return R.drawable.visa;
            case "mastercard":
                return R.drawable.mastercard;
            case "paypal":
                return R.drawable.paypal;
            case "foodelipay":
                return R.drawable.logo;
            default:
                return R.drawable.wallet_non_select;
        }
    }

}
