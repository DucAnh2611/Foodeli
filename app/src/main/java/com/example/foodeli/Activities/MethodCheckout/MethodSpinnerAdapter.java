package com.example.foodeli.Activities.MethodCheckout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.foodeli.MySqlSetUp.Schemas.General.Body.MethodSupport;
import com.example.foodeli.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodSpinnerAdapter extends ArrayAdapter<MethodSupport> {

    private ArrayList<MethodSupport> list;
    private Context context;
    private HashMap<String, Integer> mapIconSupportList;

    public MethodSpinnerAdapter(Context context, ArrayList<MethodSupport> list, HashMap<String, Integer> mapIconSupportList) {
        super(context, R.layout.items_custom_spinner, list);
        this.context = context;
        this.list = list;
        this.mapIconSupportList = mapIconSupportList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_custom_spinner, parent, false);

        ImageView spinnerImage = view.findViewById(R.id.spinner_image);
        TextView spinnerText = view.findViewById(R.id.spinner_text);

        spinnerImage.setImageResource(mapIconSupportList.get(list.get(position).getType()));
        spinnerText.setText(list.get(position).getType());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
