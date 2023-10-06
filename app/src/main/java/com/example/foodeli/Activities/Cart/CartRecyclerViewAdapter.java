package com.example.foodeli.Activities.Cart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Body.AddToCartBody;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.DeleteItemRes;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.UpdateItemRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ItemInCart>  {

    private ArrayList<GetCartRes.ProductWithImage> list;
    private Context context;
    private View view;
    Pool pool;
    private OnItemUpdate onItemUpdate;
    private int uid;
    private SupportImage supportImage = new SupportImage();

    public CartRecyclerViewAdapter(ArrayList<GetCartRes.ProductWithImage> list, Context context, int uid) {
        this.list = list;
        this.context = context;
        this.uid = uid;
    }

    @NonNull
    @Override
    public ItemInCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.items_recyclerview_cart, parent, false);
        return new ItemInCart(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemInCart holder, int position) {
        final GetCartRes.ProductWithImage item = list.get(position);

        holder.setPid(item.getPid());
        holder.cartProductName.setText(item.getName());
        holder.cartProductEstimate.setText(String.format("%sm - %sm", item.getTimeStart(), item.getTimeFinish()));
        holder.cartProductPrice.setText(String.format("%s", item.getPrice()));
        holder.cartProductUnit.setText(item.getUnit());
        holder.cartProductQuantity.setText(String.valueOf(item.getQuantity()));

        if(item.getImage() != null) {
            holder.cartProductImage.setImageBitmap(supportImage.convertBase64ToBitmap(item.getImage()));
        }
        else {
            holder.cartProductImage.setImageResource(R.drawable.cart);
        }

        holder.cartAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getQuantity() < item.getStock()) {
                    updateItem(item, uid, item.getPid(), item.getQuantity() + 1, position);
                }
                else {
                    Toast.makeText(context, "Maximum item quantity reached", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.cartMinusProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getQuantity() <= 1) {
                    deleteItem(position, uid, item.getPid());
                }
                else {
                    updateItem(item, uid, item.getPid(), item.getQuantity() - 1, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String roundFloat2Dec(double value) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(value);
    }

    public  ArrayList<GetCartRes.ProductWithImage> getItem() {return this.list;}

    private void deleteItem(int position, int uid, int pid) {
        Pool pool = new Pool();

        Call<DeleteItemRes> deleteItem = pool.getApiCallUserCart().deleteItem(uid, pid);

        deleteItem.enqueue(new Callback<DeleteItemRes>() {
            @Override
            public void onResponse(Call<DeleteItemRes> call, Response<DeleteItemRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    if(response.body().isDelete()) {
                        list.remove(position);
                        notifyItemRemoved(position);
                        onItemUpdate.onItemUpdate();
                    }
                    else {
                        Toast.makeText(context, "Fail to add more item", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteItemRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private void updateItem(GetCartRes.ProductWithImage item, int uid, int pid, int newQuantity, int position) {
        Pool pool = new Pool();

        AddToCartBody body = new AddToCartBody(uid, pid, newQuantity );

        Call<UpdateItemRes> updateQuantity = pool.getApiCallUserCart().updateItem(body);

        updateQuantity.enqueue(new Callback<UpdateItemRes>() {
            @Override
            public void onResponse(Call<UpdateItemRes> call, Response<UpdateItemRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    if(response.body().isUpdate()) {
                        item.setQuantity(newQuantity);
                        notifyItemChanged(position);
                        onItemUpdate.onItemUpdate();
                    }
                    else {
                        Toast.makeText(context, "Fail to update item", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateItemRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    public class ItemInCart extends RecyclerView.ViewHolder {

        private ShapeableImageView cartProductImage;
        private TextView cartProductName;
        private TextView cartProductEstimate;
        private TextView cartProductPrice;
        private TextView cartProductUnit;
        private ImageButton cartMinusProduct;
        private ImageButton cartAddProduct;
        private TextView cartProductQuantity;
        private int pid;

        public ItemInCart(@NonNull View itemView) {
            super(itemView);

            cartProductImage = itemView.findViewById(R.id.cart_p_image);
            cartProductName = itemView.findViewById(R.id.cart_p_name);
            cartProductEstimate = itemView.findViewById(R.id.cart_p_estimate);
            cartProductPrice = itemView.findViewById(R.id.cart_p_price);
            cartProductUnit = itemView.findViewById(R.id.cart_p_unit);
            cartMinusProduct = itemView.findViewById(R.id.cart_p_minus);
            cartAddProduct = itemView.findViewById(R.id.cart_p_add);
            cartProductQuantity = itemView.findViewById(R.id.cart_p_quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.print("clicked");
                }
            });

        }

        public void setPid(int pid) {
            this.pid = pid;
        }
    }

    public interface OnItemUpdate {
        void onItemUpdate();
    }

    public void setOnItemUpdateListener(OnItemUpdate listener) {
        this.onItemUpdate = listener;
    }

}
