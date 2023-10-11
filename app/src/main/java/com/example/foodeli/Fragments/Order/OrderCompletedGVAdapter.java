package com.example.foodeli.Fragments.Order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.foodeli.Activities.OrderDetail.OrderDetailActivity;
import com.example.foodeli.Activities.Review.Review;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.R;

import java.util.ArrayList;

public class OrderCompletedGVAdapter extends BaseAdapter {

    private ArrayList<OrderWithState> list;
    private AppCompatButton leaveReview, detail;
    private TextView idAndState, itemCount, dateFinish, total;
    private Context context;

    private IdToSerialString idToSerialString = new IdToSerialString();
    public OrderCompletedGVAdapter(ArrayList<OrderWithState> list, Context context) {
        this.list = list;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_ordercompleted, parent, false);
        }

        OrderWithState item = getItem(position);

        idAndState = convertView.findViewById(R.id.order_completed_id_and_status);
        itemCount = convertView.findViewById(R.id.order_completed_item_count);
        dateFinish = convertView.findViewById(R.id.order_completed_finish);
        total = convertView.findViewById(R.id.order_completed_total);

        idAndState.setText(idToSerialString.convertIdToSerialString(item.getOid()) + ": Completed");
        itemCount.setText(item.getItemCount() + (item.getItemCount() > 1 ?" Items" : " Item"));
        dateFinish.setText("Completed: " + item.getModified());
        total.setText(String.valueOf(item.getTotal()));

        leaveReview = convertView.findViewById(R.id.order_completed_leave_review);
        detail = convertView.findViewById(R.id.order_completed_details);

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetail = new Intent(context, OrderDetailActivity.class);
                orderDetail.putExtra("oid", item.getOid());
                context.startActivity(orderDetail);
            }
        });

        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(context, Review.class);
                reviewIntent.putExtra("oid", item.getOid());
                context.startActivity(reviewIntent);
            }
        });


        return convertView;
    }

}
