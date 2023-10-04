package com.example.foodeli.Activities.Find;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatButton;

import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.R;

import java.util.ArrayList;

public class CategoriesFindAdapter extends BaseAdapter {

    private ArrayList<Category> categories;
    private Context context;
    private OnUpdateFindData onUpdateFindData;
    private int selectId = 0;
    private AppCompatButton button;

    public CategoriesFindAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        if(categories.isEmpty()) {
            return null;
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
        Category item = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_find_filter_category, parent, false);
        }

        button = convertView.findViewById(R.id.find_filter_gridview_btn);
        button.setText(item.getName());

        if(item.getCid() == selectId || item.getP_cid() == selectId && selectId!=0) {
            button.setBackground(context.getDrawable(R.drawable.custom_input_style_stroke_green100_radius_min));
            button.setTextColor(context.getColor(R.color.green_100));
        }
        else {
            button.setBackground(context.getDrawable(R.drawable.custom_button_style_radius_min));
            button.setTextColor(context.getColor(R.color.grey_100));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectId = (selectId == item.getCid()) ? 0 : item.getCid();
                onUpdateFindData.onClickCategory();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public int getSelectId() {
        return selectId;
    }

    public void setOnUpdateFindData(OnUpdateFindData onUpdateFindData) {this.onUpdateFindData = onUpdateFindData;}
}
