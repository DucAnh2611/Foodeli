package com.example.foodeli.Fragments.Order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.example.foodeli.Activities.OrderStatus.OrderStatusActivity;
import com.example.foodeli.Activities.OrderStatus.SelectCancelReasonActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.ConfirmRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActiveGVAdapter extends BaseAdapter {

    private ArrayList<OrderWithState> listOrderActive;
    private Pool pool;
    private Context context;
    private ImageView orderIcon;
    private TextView idAndState, itemCount, orderPayed, orderTotal, orderModify;
    private AppCompatButton cancelButton, trackButton, confirmButton;
    private LinearLayout buttonLayout;
    private IdToSerialString idToSerialString = new IdToSerialString();
    private OnSelectMethodOrder onSelectMethodOrder;

    public OrderActiveGVAdapter(ArrayList<OrderWithState> listOrder, Context context) {
        this.listOrderActive = listOrder;
        this.context = context;
    }

    @Override
    public int getCount() {

        return listOrderActive.size();
    }

    @Override
    public OrderWithState getItem(int position) {
        if(listOrderActive.isEmpty()) {
            return null;
        }
        return listOrderActive.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(listOrderActive.isEmpty()) {
            return 0;
        }
        return listOrderActive.get(position).getOid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_orderactive, parent, false);

        OrderWithState item = getItem(position);

        orderIcon = convertView.findViewById(R.id.order_active_icon);
        idAndState = convertView.findViewById(R.id.order_active_id_and_status);
        itemCount = convertView.findViewById(R.id.order_active_item_count);
        orderPayed = convertView.findViewById(R.id.order_active_payed);
        orderModify = convertView.findViewById(R.id.order_active_modify);
        orderTotal = convertView.findViewById(R.id.order_active_total);
        buttonLayout = convertView.findViewById(R.id.order_active_button_layout);
        cancelButton = convertView.findViewById(R.id.order_active_cancel);
        trackButton = convertView.findViewById(R.id.order_active_track);
        confirmButton = convertView.findViewById(R.id.order_active_comfirm);

        orderIcon.setImageResource(convertIdStateToIcon(item.getStateId()));
        orderModify.setText("Modify: " + item.getModified());
        idAndState.setText(idToSerialString.convertIdToSerialString(item.getOid()) + ": " + item.getState());
        itemCount.setText(item.getItemCount() + (item.getItemCount() > 1 ? " Items" : " Item"));
        orderPayed.setText(item.getPayed() == 1 ? "Paid" : "Unpaid");
        orderTotal.setText(String.valueOf(item.getTotal()));

        if (item.getStateId() == 4) {
            buttonLayout.removeView(cancelButton);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectMethodOrder.onSelectConfirm(item, position);
                }
            });
        } else {
            buttonLayout.removeView(confirmButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectMethodOrder.onSelectCancel(item, position);
                }
            });
        }

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent OrderDetail = new Intent(context, OrderStatusActivity.class);
                OrderDetail.putExtra("oid", item.getOid());
                context.startActivity(OrderDetail);
            }
        });

        return convertView;
    }

    private int convertIdStateToIcon(int id) {
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
                return R.drawable.cancel_state;
            default:
                return R.drawable.order_select;
        }
    }

    public interface OnSelectMethodOrder {
        void onSelectCancel(OrderWithState order, int position);
        void onSelectConfirm(OrderWithState order, int position);
    }

    public void setOnSelectMethodOrder(OnSelectMethodOrder onSelectMethodOrder) {
        this.onSelectMethodOrder = onSelectMethodOrder;
    }
}


