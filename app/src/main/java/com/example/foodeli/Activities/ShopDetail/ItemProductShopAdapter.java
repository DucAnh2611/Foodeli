package com.example.foodeli.Activities.ShopDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ItemProductShopAdapter extends BaseAdapter {

    private ArrayList<GetTopProduct.ProductWithAvg> list;
    private Context context;
    private SupportImage supportImage = new SupportImage();
    private ShapeableImageView productImage;
    private TextView pName, pDesc, pPrice, pRate;
    private AppCompatButton addToCartBtn;

    public ItemProductShopAdapter(ArrayList<GetTopProduct.ProductWithAvg> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetTopProduct.ProductWithAvg getItem(int position) {
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
        return list.get(position).getPid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_recyclerview_shop_products, parent, false);
        }

        GetTopProduct.ProductWithAvg item = getItem(position);

        productImage = convertView.findViewById(R.id.item_shop_product_image);
        pRate = convertView.findViewById(R.id.item_shop_product_rate);
        pName = convertView.findViewById(R.id.item_shop_product_name);
        pDesc = convertView.findViewById(R.id.item_shop_product_desc);
        pPrice = convertView.findViewById(R.id.item_shop_product_price);
        addToCartBtn = convertView.findViewById(R.id.item_shop_product_add_to_cart);

        productImage.setImageBitmap(supportImage.convertBase64ToBitmap(item.getPImage()));
        pRate.setText(String.valueOf(item.getAvg()));
        pName.setText(item.getName());
        pDesc.setText(item.getShortDesc());
        pPrice.setText(String.valueOf(item.getPrice()));

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}
