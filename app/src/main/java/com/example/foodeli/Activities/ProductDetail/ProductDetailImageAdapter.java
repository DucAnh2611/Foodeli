package com.example.foodeli.Activities.ProductDetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.GetProductInfo;
import com.example.foodeli.R;

import java.util.ArrayList;

public class ProductDetailImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<GetProductInfo.ImageWithId> imageIds = null;

    public ProductDetailImageAdapter(ArrayList<GetProductInfo.ImageWithId> imageIds, Context context){
        this.imageIds = imageIds;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_viewpager_prroductdetail, container, false);

        ImageView imageView = view.findViewById(R.id.image_viewpager_pd);

        if(!imageIds.isEmpty()) {
            byte[] decodedSString = Base64.decode(imageIds.get(position).getImage(), Base64.DEFAULT);
            Bitmap decodedSByte = BitmapFactory.decodeByteArray(decodedSString, 0, decodedSString.length);
            imageView.setImageBitmap(decodedSByte);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getCount() {
        return imageIds.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
