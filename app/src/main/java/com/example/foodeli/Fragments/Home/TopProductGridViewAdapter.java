package com.example.foodeli.Fragments.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodeli.Activities.ProductDetail.ProductDetail;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TopProductGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GetTopProduct.ProductWithAvg> topProducts;

    public TopProductGridViewAdapter(ArrayList<GetTopProduct.ProductWithAvg> topProducts, Context context) {
        this.topProducts = topProducts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return topProducts.size();
    }

    @Override
    public Object getItem(int position) {
        if(topProducts.isEmpty()) {
            return null;
        }
        return topProducts.get(position);
    }

    @Override
    public long getItemId(int position){
        if(topProducts.isEmpty()) {
            return 0;
        }
        return topProducts.get(position).getPid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_girdview_topproduct, parent, false);
        }

        if(topProducts.isEmpty()) {
            return convertView;
        }

        TextView rate = convertView.findViewById(R.id.top_p_rate_avg);
        TextView name = convertView.findViewById(R.id.top_p_name);
        TextView timeEstimate = convertView.findViewById(R.id.top_p_time);
        ShapeableImageView pimage = convertView.findViewById(R.id.top_p_p_image);
        ShapeableImageView simage = convertView.findViewById(R.id.top_p_s_image);

        GetTopProduct.ProductWithAvg item = topProducts.get(position);

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);

        rate.setText( String.format(df.format(item.getAvg())) );

        name.setText(item.getName());

        String estimate = item.getTimeStart() + "m-" + item.getTimeFinish() + "m";
        timeEstimate.setText(estimate);

        if(!item.getPImage().equals("")) {
            byte[] decodedPString = Base64.decode(item.getPImage(), Base64.DEFAULT);
            Bitmap decodedPByte = BitmapFactory.decodeByteArray(decodedPString, 0, decodedPString.length);
            pimage.setImageBitmap(decodedPByte);
        }
        if(!item.getSImage().equals("")) {
            byte[] decodedSString = Base64.decode(item.getSImage(), Base64.DEFAULT);
            Bitmap decodedSByte = BitmapFactory.decodeByteArray(decodedSString, 0, decodedSString.length);
            simage.setImageBitmap(decodedSByte);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetailIntent = new Intent(context, ProductDetail.class);
                productDetailIntent.putExtra("pid", item.getPid());
                context.startActivity(productDetailIntent);

            }
        });

        return convertView;
    }

    public void addItems(ArrayList<GetTopProduct.ProductWithAvg> list) {this.topProducts.addAll(list);}
}
