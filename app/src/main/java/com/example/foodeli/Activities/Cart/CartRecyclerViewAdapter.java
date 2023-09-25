package com.example.foodeli.Activities.Cart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ItemInCart> {

    private ArrayList<GetCartRes.ProductWithImage> list;
    private Context context;
    private View view;

    public CartRecyclerViewAdapter(ArrayList<GetCartRes.ProductWithImage> list, Context context) {
        this.list = list;
        this.context = context;
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
            byte[] decodedString = Base64.decode(item.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.cartProductImage.setImageBitmap(decodedByte);
        }
        else {
            holder.cartProductImage.setImageResource(R.drawable.cart);
        }

        holder.cartAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.cartMinusProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void deleteItem(int position) {
        // Remove the item from your data source based on position
        list.remove(position);

        // Notify adapter about the removal so that it can update UI.
        notifyDataSetChanged();
    }
    public class ItemInCart extends RecyclerView.ViewHolder {

        private ShapeableImageView cartProductImage;
        private TextView cartProductName;
        private TextView cartProductEstimate;
        private TextView cartProductPrice;
        private TextView cartProductUnit;
        private ImageButton cartMinusProduct;
        private ImageButton cartAddProduct;
        private EditText cartProductQuantity;
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

}
