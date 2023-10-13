package com.example.foodeli.Activities.ProductReview;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response.GetProductReviewRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ReviewListAdapter extends BaseAdapter {

    private ArrayList<GetProductReviewRes.ReviewWithImage> list;
    private Context context;
    private ShapeableImageView userAva;
    private TextView uName, uDesc;
    private LinearLayout uRate;
    private GridView reviewImage;
    private SupportImage supportImage = new SupportImage();

    public ReviewListAdapter(ArrayList<GetProductReviewRes.ReviewWithImage> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetProductReviewRes.ReviewWithImage getItem(int position) {
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
        return list.get(position).getRid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.items_listview_review_product, parent, false);

        GetProductReviewRes.ReviewWithImage item = getItem(position);
        ArrayList<GetProductReviewRes.ImageReview> listImage = getItem(position).getImages();

        userAva = convertView.findViewById(R.id.item_productdetail_review_user_image);
        uName = convertView.findViewById(R.id.item_productdetail_review_user_name);
        uDesc = convertView.findViewById(R.id.item_productdetail_review_user_desc);
        uRate = convertView.findViewById(R.id.item_productdetail_review_user_rate_layout);
        reviewImage = convertView.findViewById(R.id.item_productdetail_review_user_content);

        userAva.setImageBitmap(supportImage.convertBase64ToBitmap(item.getAvatar()));
        uName.setText(item.getFullname());
        uDesc.setText(item.getDesc());
        for (int i = 0; i < uRate.getChildCount(); i++) {
            ImageView view = (ImageView) uRate.getChildAt(i);
            int color = R.color.grey_300;
            if(i < item.getRate()) {
                color = R.color.orange_400;
            }
            view.setColorFilter(context.getColor(color));
        }

        return convertView;
    }

    public void setList(ArrayList<GetProductReviewRes.ReviewWithImage> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
