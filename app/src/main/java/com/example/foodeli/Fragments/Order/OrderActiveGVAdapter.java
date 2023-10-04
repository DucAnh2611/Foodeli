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
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_orderactive, parent, false);
        }

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
        itemCount.setText(item.getItemCount() + (item.getItemCount() > 1 ?" Items" : " Item"));
        orderPayed.setText(item.getPayed() == 1 ? "Paid" : "Unpaid");
        orderTotal.setText(String.valueOf(item.getTotal()));

        if(item.getStateId() == 4) {
            buttonLayout.removeView(cancelButton);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupConfirm(parent, position);
                }
            });
        }
        else{
            buttonLayout.removeView(confirmButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cancelOrder = new Intent(context, SelectCancelReasonActivity.class);
                    cancelOrder.putExtra("oid", item.getOid());
                    context.startActivity(cancelOrder);
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

    private void showPopupConfirm(ViewGroup parent, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_order, parent, false);
        builder.setView(view);

        TextView title = view.findViewById(R.id.dialog_confirm_title);
        TextView message = view.findViewById(R.id.dialog_confirm_message);
        AppCompatButton cancelBtn = view.findViewById(R.id.dialog_cancel_btn);
        AppCompatButton confirmBtn = view.findViewById(R.id.dialog_confirm_btn);

        title.setText("Confirm Order");
        message.setText("Are you sure to confirm this order?");
        cancelBtn.setText("Cancel");
        confirmBtn.setText("Confirm");

        AlertDialog alertDialog = builder.create();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pool = new Pool();

                Gson gson = new Gson();
                SharedPreferences mPrefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                String json = mPrefs.getString("user", "");
                User user = gson.fromJson(json, User.class);

                Call<ConfirmRes> confirmOrder = pool.getApiCallUserOrder().confirmOrder(user.getId(), (int) getItemId(position));
                confirmOrder.enqueue(new Callback<ConfirmRes>() {
                    @Override
                    public void onResponse(Call<ConfirmRes> call, Response<ConfirmRes> response) {
                        if (!response.isSuccessful()) {
                            Gson gson = new GsonBuilder().create();
                            ResponseApi res;
                            try {
                                res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                                System.out.println(res.getMessage());
                            } catch (IOException e) {
                                System.out.println("parse err false");
                            }
                        }
                        else {
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ConfirmRes> call, Throwable t) {
                        System.out.print(t.getMessage());
                    }
                });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}


