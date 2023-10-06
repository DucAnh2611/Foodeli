package com.example.foodeli.Fragments.Order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodeli.Activities.OrderDetail.OrderDetailActivity;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.R;

import java.util.ArrayList;

public class OrderCancelledGVAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OrderWithState> list;
    private TextView idAndState, itemCount, cancelledAt, total;
    private IdToSerialString idToSerialString = new IdToSerialString();

    public OrderCancelledGVAdapter(ArrayList<OrderWithState> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public OrderWithState getItem(int position) {
        if(list.isEmpty()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(list.isEmpty()) {
            return 0;
        }
        return list.get(position).getOid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_ordercancelled, parent, false);
        }
        OrderWithState item = getItem(position);

        idAndState = convertView.findViewById(R.id.order_cancelled_id_and_status);
        itemCount = convertView.findViewById(R.id.order_cancelled_item_count);
        cancelledAt = convertView.findViewById(R.id.order_cancelled_finish);
        total = convertView.findViewById(R.id.order_cancelled_total);

        idAndState.setText(idToSerialString.convertIdToSerialString(item.getOid()) + ": Cancelled");
        itemCount.setText(item.getItemCount() + (item.getItemCount() > 1 ?" Items" :" Item"));
        cancelledAt.setText(item.getModified());
        total.setText(String.valueOf(item.getTotal()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetail = new Intent(context, OrderDetailActivity.class);
                orderDetail.putExtra("oid", item.getOid());
                context.startActivity(orderDetail);
            }
        });

        return convertView;
    }
}
