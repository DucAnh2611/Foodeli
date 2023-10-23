package com.example.foodeli.Fragments.ShopEmployee;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Fragments.Shop.OnMethodShopManage;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllUserInShop;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ShopEmployeeAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<GetAllUserInShop.UserInShop> list;
    private SupportImage supportImage = new SupportImage();
    private ShapeableImageView eImage;
    private TextView eRole, eName, eUpdate;
    private LinearLayout eEdit, eDelete;
    private OnMethodShopManage onMethodShopManage;

    public ShopEmployeeAdapter(Context context, ArrayList<GetAllUserInShop.UserInShop> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list.isEmpty()) {
            return 0;
        }
        return list.size();
    }

    @Override
    public GetAllUserInShop.UserInShop getItem(int position) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_employee_shop, parent, false);
        GetAllUserInShop.UserInShop item = getItem(position);

        eImage = convertView.findViewById(R.id.item_shop_list_emp_image);
        eRole = convertView.findViewById(R.id.item_shop_list_emp_role);
        eName = convertView.findViewById(R.id.item_shop_list_emp_name);
        eUpdate = convertView.findViewById(R.id.item_shop_list_emp_update);
        eEdit = convertView.findViewById(R.id.item_shop_list_emp_edit);
        eDelete = convertView.findViewById(R.id.item_shop_list_emp_delete);

        if(!item.getAvatar().equals("")) {
            Bitmap image = supportImage.convertBase64ToBitmap(item.getAvatar());
            eImage.setImageBitmap(image);
        }
        eRole.setText(item.getRole());
        eName.setText(item.getName());
        eUpdate.setText("Update: " + item.getUpdate());

        eEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.getRole().equals("Owner")) onMethodShopManage.onEdit(position, item);
                else {
                    Toast.makeText(context, "Can not update Shop owner", Toast.LENGTH_SHORT).show();
                }
            }
        });

        eDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.getRole().equals("Owner")) onMethodShopManage.onDelete(position, item.getId());
                else {
                    Toast.makeText(context, "Can not delete Shop owner", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    public void setOnMethodShopManage(OnMethodShopManage onMethodShopManage) {
        this.onMethodShopManage = onMethodShopManage;
    }

    public void setList(ArrayList<GetAllUserInShop.UserInShop> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
