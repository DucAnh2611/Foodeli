package com.example.foodeli.Fragments.Shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ListShopGridviewAdapter extends BaseAdapter {

    private ArrayList<GetAllShopUserHaveResponse.ShopWithDetail> list;
    private Context context;
    private SupportImage supportImage = new SupportImage();
    private ShapeableImageView shopImage;
    private TextView rate, role, name, desc, sold, product;
    private OnSelectShop onSelectShop;

    public ListShopGridviewAdapter(ArrayList<GetAllShopUserHaveResponse.ShopWithDetail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetAllShopUserHaveResponse.ShopWithDetail getItem(int position) {
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
        return list.get(position).getSid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_list_shop_user, parent,false);

        GetAllShopUserHaveResponse.ShopWithDetail item = getItem(position);

        shopImage = convertView.findViewById(R.id.user_shop_image);
        rate = convertView.findViewById(R.id.user_shop_rate);
        role = convertView.findViewById(R.id.user_shop_role);
        name = convertView.findViewById(R.id.user_shop_name);
        desc = convertView.findViewById(R.id.user_shop_desc);
        sold = convertView.findViewById(R.id.user_shop_sold);
        product = convertView.findViewById(R.id.user_shop_product);

        shopImage.setImageBitmap(supportImage.convertBase64ToBitmap(item.getAvatar()));
        rate.setText(String.valueOf(item.getShopRating()));
        role.setText(item.getRole());
        name.setText(item.getName());
        desc.setText(item.getDesc());
        sold.setText(String.format("%d Sold", item.getSold()));
        product.setText(String.format("%d Product", item.getProductQuantity()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectShop.onSelectShop(item);
            }
        });

        return convertView;
    }

    public interface OnSelectShop {
        void onSelectShop(GetAllShopUserHaveResponse.ShopWithDetail shop);
    }

    public void setOnSelectShop(OnSelectShop onSelectShop) {
        this.onSelectShop = onSelectShop;
    }
}
