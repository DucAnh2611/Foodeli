package com.example.foodeli.Activities.PoolVoucherShop;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.foodeli.Activities.Voucher.FindVoucherAdapter;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Body.SaveVoucherBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.GetAllVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.SaveVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoolVoucherGridviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Voucher> listVou;
    private TextView quan, title, desc, code;
    private AppCompatButton saveVou;
    private Pool pool;

    public PoolVoucherGridviewAdapter(ArrayList<Voucher> listVou, Context context) {
        this.context = context;
        this.listVou = listVou;
    }

    @Override
    public int getCount() {
        return listVou.size();
    }

    @Override
    public Voucher getItem(int position) {
        if(listVou.isEmpty()) {
            return null;
        }
        return listVou.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(listVou.isEmpty()) {
            return 0;
        }
        return listVou.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_pool_shop_voucher, parent, false);

        Voucher item = getItem(position);

        Gson gson = new Gson();
        SharedPreferences mPrefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        User user = gson.fromJson(json, User.class);

        code = convertView.findViewById(R.id.item_pool_voucher_code);
        title = convertView.findViewById(R.id.item_pool_voucher_title);
        desc = convertView.findViewById(R.id.item_pool_voucher_desc);
        quan = convertView.findViewById(R.id.item_pool_voucher_quantity);
        saveVou = convertView.findViewById(R.id.item_pool_voucher_save);

        code.setText(item.getCode());
        title.setText(item.getTitle());
        desc.setText(item.getDesc());
        quan.setText(String.format("Limit: %d", item.getLimit()));

        saveVou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveVoucherForUser(user.getId(), item.getId(), "id");
            }
        });

        return convertView;
    }

    private void saveVoucherForUser(int uid, int vid, String type) {
        pool = new Pool();

        SaveVoucherBody body = new SaveVoucherBody(uid, type, String.valueOf(vid));
        Call<SaveVoucherRes> saveVoucher = pool.getApiCallUserVoucher().saveVoucher(body);
        saveVoucher.enqueue(new Callback<SaveVoucherRes>() {
            @Override
            public void onResponse(Call<SaveVoucherRes> call, Response<SaveVoucherRes> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveVoucherRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

}
