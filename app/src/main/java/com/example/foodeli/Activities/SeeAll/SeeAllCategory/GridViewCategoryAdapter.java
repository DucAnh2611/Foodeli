package com.example.foodeli.Activities.SeeAll.SeeAllCategory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class GridViewCategoryAdapter extends BaseAdapter {

    ArrayList<Category> categories;
    Context context;

    public GridViewCategoryAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        if(categories.isEmpty()) {
            return  null;
        }
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(categories.isEmpty()) {
            return 0;
        }
        return categories.get(position).getCid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_gridview_allcategory, parent, false);
        }

        ShapeableImageView img = convertView.findViewById(R.id.cate_all_gv_img);
        TextView c_name = convertView.findViewById(R.id.cate_all_gv_name);
        Category cate = categories.get(position);

        if(cate.getImage() != null) {
            byte[] decodedPString = Base64.decode(cate.getImage(), Base64.DEFAULT);
            Bitmap decodedPByte = BitmapFactory.decodeByteArray(decodedPString, 0, decodedPString.length);

            img.setImageBitmap(decodedPByte);
        }

        c_name.setText(cate.getName());

        return convertView;
    }
}
