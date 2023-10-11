package com.example.foodeli.Activities.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderItemsRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;


public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductView> {

    private ArrayList<OrderItemsRes.OrderItems> list;
    private OnSelectProductReview onSelectProductReview;
    private Context context;
    private SupportImage supportImage = new SupportImage();
    private OrderItemsRes.OrderItems selectItem;

    public ProductRecyclerViewAdapter(ArrayList<OrderItemsRes.OrderItems> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.items_recyclerview_review_products, parent, false);
        return new ProductView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductView holder, int position) {
        final OrderItemsRes.OrderItems item = list.get(position);

        holder.productImage.setImageBitmap(supportImage.convertBase64ToBitmap(item.getImage()));
        holder.productName.setText(item.getPname());
        holder.setItems(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductView extends RecyclerView.ViewHolder {

        private ShapeableImageView productImage;
        private TextView productName;
        private OrderItemsRes.OrderItems item;
        public ProductView(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.review_product_image);
            productName = itemView.findViewById(R.id.review_product_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem = item;
                    onSelectProductReview.onSelectProductReview();
                }
            });
        }

        public void setItems(OrderItemsRes.OrderItems item) {
            this.item = item;
        }

    }

    public OrderItemsRes.OrderItems getSelectItem() {
        return selectItem;
    }

    public void setList(ArrayList<OrderItemsRes.OrderItems> list) {
        this.list = list;
    }

    public interface OnSelectProductReview {
        void onSelectProductReview();
    }

    public void setOnSelectProductReview(OnSelectProductReview onSelectProductReview) {this.onSelectProductReview = onSelectProductReview;}
}
