package com.example.foodeli.Activities.MethodCheckout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.example.foodeli.R;

import java.util.ArrayList;

public class UserPaymentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MethodWithTypeName> list;
    private OnSelectMethodUpdate onSelectMethodUpdate;
    private TextView number, date;

    public UserPaymentAdapter(ArrayList<MethodWithTypeName> list, Context context) {
        this.context = context;
        this.list = list;
    };

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MethodWithTypeName getItem(int position) {
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
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_user_payment_subgridview, parent, false);
        }

        MethodWithTypeName item = getItem(position);

        number = convertView.findViewById(R.id.user_payment_subgridview_number);
        date = convertView.findViewById(R.id.user_payment_subgridview_expire);

        number.setText(item.getNumber());

        String stringDate = item.getExpired();
        String[] splitDate = stringDate.split("-");

        int year = Integer.parseInt(splitDate[0].substring(2));
        int month = Integer.parseInt(splitDate[1]);

        date.setText(String.format("Expire at: %02d/%02d", month, year));

        return convertView;
    }
}
