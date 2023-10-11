package com.example.foodeli.Activities.OrderDetail;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderItemsRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class OrderDetailProductsAdapter extends BaseAdapter {

    private ArrayList<OrderItemsRes.OrderItems> items;
    private Context context;
    private TextView name, price, unit, quan;
    private ShapeableImageView productImage;
    private SupportImage supportImage = new SupportImage();

    public OrderDetailProductsAdapter(ArrayList<OrderItemsRes.OrderItems> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public OrderItemsRes.OrderItems getItem(int position) {
        if(items.isEmpty()) {
            return null;
        }
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(items.isEmpty()) {
            return 0;
        }
        return items.get(position).getPid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_orderdetail, parent, false);
        }

        OrderItemsRes.OrderItems item = getItem(position);

        name = convertView.findViewById(R.id.orderdetail_p_name);
        price = convertView.findViewById(R.id.orderdetail_p_price);
        unit = convertView.findViewById(R.id.orderdetail_p_unit);
        quan = convertView.findViewById(R.id.orderdetail_p_quan);
        productImage = convertView.findViewById(R.id.orderdetail_p_image);

        name.setText(item.getPname());
        price.setText(String.valueOf(item.getPrice()));
        unit.setText(String.valueOf(item.getUnit()));
        quan.setText("Quan: " + item.getQuantity());
        productImage.setImageBitmap(supportImage.convertBase64ToBitmap(item.getImage()));

        return convertView;
    }
}
