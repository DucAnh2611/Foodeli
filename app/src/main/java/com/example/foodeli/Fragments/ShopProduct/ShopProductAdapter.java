package com.example.foodeli.Fragments.ShopProduct;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.foodeli.Fragments.Shop.OnMethodShopManage;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ShopProductAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<GetTopProduct.ProductWithAvg> list;
    private SupportImage supportImage = new SupportImage();
    private ShapeableImageView pImage;
    private TextView pRate, pName, pDesc, pPrice, pUnit, pUpdate;
    private LinearLayout pEdit, pDelete;
    private OnMethodShopManage onMethodShopManage;

    public ShopProductAdapter(Context context, ArrayList<GetTopProduct.ProductWithAvg> list) {
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
    public GetTopProduct.ProductWithAvg getItem(int position) {
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
        return list.get(position).getPid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_product_shop, parent, false);
        GetTopProduct.ProductWithAvg item = getItem(position);

        pImage = convertView.findViewById(R.id.item_shop_list_product_image);
        pRate = convertView.findViewById(R.id.item_shop_list_product_rate);
        pName = convertView.findViewById(R.id.item_shop_list_product_name);
        pDesc = convertView.findViewById(R.id.item_shop_list_product_desc);
        pPrice = convertView.findViewById(R.id.item_shop_list_product_price);
        pUnit = convertView.findViewById(R.id.item_shop_list_product_unit);
        pUpdate = convertView.findViewById(R.id.item_shop_list_product_update);
        pEdit = convertView.findViewById(R.id.item_shop_list_product_edit);
        pDelete = convertView.findViewById(R.id.item_shop_list_product_delete);

        Bitmap imageProduct = supportImage.convertBase64ToBitmap(item.getPImage());

        pImage.setImageBitmap(item.getDelete() == 0 ? imageProduct : supportImage.toGrayscale(imageProduct));
        pRate.setText(String.valueOf(item.getAvg()));
        pName.setText(item.getName());
        pDesc.setText(item.getShortDesc());
        pPrice.setText(String.valueOf(item.getPrice()));
        pUnit.setText(item.getUnit());
        pUpdate.setText("Update: " + item.getModified());

        pEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMethodShopManage.onEdit(position, item);
            }
        });

        pDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMethodShopManage.onDelete(position, item.getPid());
            }
        });


        return convertView;
    }

    public void setOnMethodShopManage(OnMethodShopManage onMethodShopManage) {
        this.onMethodShopManage = onMethodShopManage;
    }

    public void setList(ArrayList<GetTopProduct.ProductWithAvg> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
