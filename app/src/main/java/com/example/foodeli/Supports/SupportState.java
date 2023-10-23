package com.example.foodeli.Supports;

import com.example.foodeli.R;

public class SupportState {

    public int convertIdStateToIcon(int id) {
        switch(id) {
            case 1:
                return R.drawable.receive_order;
            case 2:
                return R.drawable.cooking_state;
            case 3:
                return R.drawable.delivery_state;
            case 4:
                return R.drawable.arrived_state;
            case 5:
                return R.drawable.completed_state;
            case 6:
                return R.drawable.cancel_state;
            default:
                return R.drawable.order_select;
        }
    }

}
