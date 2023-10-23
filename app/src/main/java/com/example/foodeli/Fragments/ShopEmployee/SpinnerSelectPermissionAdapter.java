package com.example.foodeli.Fragments.ShopEmployee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.foodeli.MySqlSetUp.Schemas.General.Body.ShopPermission;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;

import java.util.ArrayList;

public class SpinnerSelectPermissionAdapter extends ArrayAdapter<ShopPermission> {
    private ArrayList<ShopPermission> list;
    private Context context;

    public SpinnerSelectPermissionAdapter(Context context, ArrayList<ShopPermission> list) {
        super(context, R.layout.items_custom_spinner_permission, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_custom_spinner_permission, parent, false);

        TextView spinnerText = view.findViewById(R.id.spinner_text);

        spinnerText.setText(list.get(position).getType());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
