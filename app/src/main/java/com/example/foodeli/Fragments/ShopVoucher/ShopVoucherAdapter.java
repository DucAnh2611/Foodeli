package com.example.foodeli.Fragments.ShopVoucher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodeli.Fragments.Shop.OnMethodShopManage;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportDate;
import com.example.foodeli.Supports.SupportImage;

import java.util.ArrayList;

public class ShopVoucherAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher> list;
    private SupportImage supportImage = new SupportImage();
    private TextView vCode, vTitle, vDesc, vLimit, vUpdate;
    private LinearLayout vDelete;
    private OnMethodShopManage onMethodShopManage;
    private SupportDate supportDate = new SupportDate();

    public ShopVoucherAdapter(Context context, ArrayList<com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher> list) {
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
    public Voucher getItem(int position) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_voucher_shop, parent, false);
        Voucher item = getItem(position);

        vCode = convertView.findViewById(R.id.item_shop_list_voucher_code);
        vTitle = convertView.findViewById(R.id.item_shop_list_voucher_title);
        vDesc = convertView.findViewById(R.id.item_shop_list_voucher_desc);
        vLimit = convertView.findViewById(R.id.item_shop_list_voucher_quantity);
        vUpdate = convertView.findViewById(R.id.item_shop_list_voucher_update);
        vDelete = convertView.findViewById(R.id.item_shop_list_voucher_delete);

        vCode.setText(item.getCode());
        vTitle.setText(item.getTitle());
        vDesc.setText(item.getDesc());
        vLimit.setText(String.format("Limit: %d", item.getLimit()));
        vUpdate.setText("Expire: " + supportDate.TimeAfterSecond(supportDate.ConvertLAtoVN(item.getCreate()), item.getExpired()));

        vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMethodShopManage.onDelete(position, item.getId());
            }
        });


        return convertView;
    }

    public void setOnMethodShopManage(OnMethodShopManage onMethodShopManage) {
        this.onMethodShopManage = onMethodShopManage;
    }

    public void setList(ArrayList<Voucher> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
