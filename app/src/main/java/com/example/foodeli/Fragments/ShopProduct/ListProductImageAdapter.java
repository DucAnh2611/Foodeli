package com.example.foodeli.Fragments.ShopProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.CreateProductRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ListProductImageAdapter extends RecyclerView.Adapter<ListProductImageAdapter.ImageViewHolder> {

    private ArrayList<CreateProductRes.Image> list;
    private Context context;
    private SupportImage supportImage = new SupportImage();
    private OnDeleteImage onDeleteImage;

    public ListProductImageAdapter(Context context, ArrayList<CreateProductRes.Image> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_recyclerview_form_product_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int pos) {
        int position = holder.getLayoutPosition();
        CreateProductRes.Image item = list.get(position);

        holder.image.setImageBitmap(supportImage.convertBase64ToBitmap(item.getBase64()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteImage.onDelete(item.getIid(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView image;
        private LinearLayout delete;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.form_product_image);
            delete = itemView.findViewById(R.id.form_product_delete);
        }
    }

    public interface OnDeleteImage {
        void onDelete(int iid, int position);
    }

    public void setOnDeleteImage(OnDeleteImage onDeleteImage) {
        this.onDeleteImage = onDeleteImage;
    }

    public void setList(ArrayList<CreateProductRes.Image> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<CreateProductRes.Image> getList() {
        return list;
    }
}
