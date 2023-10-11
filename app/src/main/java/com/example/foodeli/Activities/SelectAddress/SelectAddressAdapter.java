package com.example.foodeli.Activities.SelectAddress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.example.foodeli.R;

import java.util.ArrayList;

public class SelectAddressAdapter extends BaseAdapter {

    private Pool pool = null;
    private int tempSelect = -1;
    private String tempName = "";
    private ArrayList<Address> listAdd;
    private Context context;
    private int[] icon = {R.drawable.address_select, R.drawable.address_bold};

    private ImageView iconImage;
    private TextView addressText;
    private RelativeLayout selectLayout;
    private View innerSelect;
    private OnSelectAddress onSelectAddress;

    public SelectAddressAdapter(ArrayList<Address> listAdd, int aid, String aname, Context context) {
        this.listAdd = listAdd;
        this.tempSelect = aid;
        this.context = context;
        this.tempName = aname;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.items_select_address_gridview, parent, false);
        }

        Address item = listAdd.get(position);

        iconImage = convertView.findViewById(R.id.select_address_icon);
        addressText = convertView.findViewById(R.id.select_address_text);
        selectLayout = convertView.findViewById(R.id.select_address_layout);
        innerSelect = convertView.findViewById(R.id.select_address_inner);

        if(item.getAid() == tempSelect) {
            iconImage.setImageResource(R.drawable.address_bold);
            innerSelect.setVisibility(View.VISIBLE);
            addressText.setTextColor(context.getColor(R.color.green_100));
            convertView.setBackgroundResource(R.drawable.custom_button_style_sec);
        }
        else{
            iconImage.setImageResource(R.drawable.address_select);
            innerSelect.setVisibility(View.INVISIBLE);
            addressText.setTextColor(context.getColor(R.color.black));
            convertView.setBackgroundResource(R.drawable.custom_input_style);
        }

        addressText.setText(item.getAddress());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempSelect == item.getAid()) {
                    tempSelect = 0;
                }
                else {
                    tempSelect = item.getAid();
                }
                onSelectAddress.onSelectAddress(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void setListAdd(ArrayList<Address> listAdd) {
        this.listAdd = listAdd;
        notifyDataSetChanged();
    }

    public interface OnSelectAddress {
        void onSelectAddress(int position);
    }

    public void setOnSelectAddress(OnSelectAddress onSelectAddress) {
        this.onSelectAddress = onSelectAddress;
    }
}
