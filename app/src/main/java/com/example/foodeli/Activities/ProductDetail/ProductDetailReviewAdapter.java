package com.example.foodeli.Activities.ProductDetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response.GetProductReviewRes;
import com.example.foodeli.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ProductDetailReviewAdapter extends BaseAdapter {

    private ArrayList<GetProductReviewRes.ReviewWithImage> reviews = null;
    private Context context = null;

    public ProductDetailReviewAdapter(ArrayList<GetProductReviewRes.ReviewWithImage> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        if(reviews.isEmpty()) {
           return null;
        }
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reviews.get(position).getPid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
//        if(itemView==null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.items_listview_productdetail_review, parent, false);

            GetProductReviewRes.ReviewWithImage item = reviews.get(position);

            ShapeableImageView userAvatar = itemView.findViewById(R.id.productdetail_review_user_image);
            TextView userName = itemView.findViewById(R.id.productdetail_review_user_name);
            TextView userTitle = itemView.findViewById(R.id.productdetail_review_user_title);
            TextView userDesc = itemView.findViewById(R.id.productdetail_review_user_desc);

            LinearLayout userRateLayout = itemView.findViewById(R.id.productdetail_user_rate_layout);

            userName.setText(item.getFullname());
            userTitle.setText(item.getTitle());
            userDesc.setText(item.getDesc());

            if (!item.getAvatar().equals("")) {
                byte[] decodedPString = Base64.decode(item.getAvatar(), Base64.DEFAULT);
                Bitmap decodedPByte = BitmapFactory.decodeByteArray(decodedPString, 0, decodedPString.length);
                userAvatar.setImageBitmap(decodedPByte);
            }

            if (item.getImages().isEmpty()) {
                GridView gridView = itemView.findViewById(R.id.productdetail_review_user_gridview_image);
                LinearLayout mainReview = itemView.findViewById(R.id.productdetail_review_main_holder);

                mainReview.removeView(gridView);
            }

            int rating = item.getRate();
            for (int i = 0; i < 5; i++) {
                ImageView star = new ImageView(context);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
                star.setImageResource(R.drawable.rate);
                if(i<=rating ) star.setColorFilter(context.getColor(R.color.orange_400));
                else star.setColorFilter(context.getColor(R.color.grey_300));
                star.setLayoutParams(params);

                userRateLayout.addView(star);
            }
//        }

        return itemView;
    }

    public void setReviews(ArrayList<GetProductReviewRes.ReviewWithImage> reviews) {
        this.reviews = reviews;
    }
}
