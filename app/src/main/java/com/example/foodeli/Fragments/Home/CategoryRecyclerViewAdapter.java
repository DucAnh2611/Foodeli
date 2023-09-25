package com.example.foodeli.Fragments.Home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.R;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Category> listCategory;
    private Context context;
    private View view;

    public CategoryRecyclerViewAdapter(ArrayList<Category> listCategory, Context context) {
        this.listCategory = listCategory;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.items_recyclerview_categories, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Category modal = listCategory.get(position);
        holder.CategoryTV.setText(modal.getName());
        holder.setCid(modal.getCid());

        if(modal.getImage() != null) {
            byte[] decodedString = Base64.decode(modal.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.CategoryImage.setImageBitmap(decodedByte);
        }
        else {
            holder.CategoryImage.setImageResource(R.drawable.category_default);
        }


    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView CategoryTV;
        private ImageView CategoryImage;
        private int cid ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CategoryTV = itemView.findViewById(R.id.cate_rv_tv);
            CategoryImage = itemView.findViewById(R.id.cate_rv_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(cid);
                }
            });
        }

        public void setCid(int cid) {
            this.cid = cid;
        }
    }
}
