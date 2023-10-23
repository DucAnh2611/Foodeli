package com.example.foodeli.Fragments.ShopEmployee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class UserFoundAdapter extends BaseAdapter {

    private ArrayList<User> list;
    private Context context;
    private LinearLayout addBtn;
    private TextView uName, uEmail;
    private OnAddUserToShop onAddUserToShop;
    private ShapeableImageView uImage;
    private SupportImage supportImage = new SupportImage();

    public UserFoundAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public User getItem(int position) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_employee_shop_find, parent, false);
        User item = getItem(position);

        uName = convertView.findViewById(R.id.item_dialog_find_emp_name);
        uEmail = convertView.findViewById(R.id.item_dialog_find_emp_email);
        uImage = convertView.findViewById(R.id.item_dialog_find_emp_image);
        addBtn = convertView.findViewById(R.id.item_dialog_find_emp_add);

        uName.setText(item.getName());
        uEmail.setText(item.getEmail());
        if(!item.getAvatar().equals("")) {
            uImage.setImageBitmap(supportImage.convertBase64ToBitmap(item.getAvatar()));
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddUserToShop.onAddUser(item);
            }
        });

        return convertView;
    }

    public interface OnAddUserToShop {
        void onAddUser(User user);
    }

    public void setOnAddUserToShop(OnAddUserToShop onAddUserToShop) {
        this.onAddUserToShop = onAddUserToShop;
    }
}
