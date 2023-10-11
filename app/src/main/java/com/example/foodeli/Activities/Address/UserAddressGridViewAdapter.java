package com.example.foodeli.Activities.Address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.example.foodeli.R;

import java.util.ArrayList;

public class UserAddressGridViewAdapter extends BaseAdapter {

    private ArrayList<Address> listAdd;
    private Context context;
    private ImageButton updateAddress, deleteAddress;
    private TextView address;
    private OnSelectMethodAddress onSelectMethodAddress;

    public UserAddressGridViewAdapter(ArrayList<Address> listAdd, Context context) {
        this.listAdd = listAdd;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listAdd.size();
    }

    @Override
    public Address getItem(int position) {
        if(listAdd.isEmpty()) {
            return null;
        }
        return listAdd.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(listAdd.isEmpty()) {
            return 0;
        }
        return listAdd.get(position).getAid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_user_address_gridview, parent, false);
        }

        Address item = getItem(position);

        updateAddress = convertView.findViewById(R.id.item_user_address_update);
        deleteAddress = convertView.findViewById(R.id.item_user_address_delete);
        address = convertView.findViewById(R.id.item_user_address_text);

        address.setText(item.getAddress());

        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectMethodAddress.onSelectUpdate(position);
            }
        });

        deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectMethodAddress.onSelectDelete(position);
            }
        });

        return convertView;
    }

    public void setListAdd(ArrayList<Address> listAdd) {this.listAdd = listAdd; notifyDataSetChanged();}

    public void setOnSelectMethodAddress(OnSelectMethodAddress onSelectMethodAddress) {this.onSelectMethodAddress = onSelectMethodAddress;}
}
