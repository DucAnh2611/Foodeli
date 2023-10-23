package com.example.foodeli.Fragments.ShopOrder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.foodeli.Activities.OrderDetail.OrderDetailActivity;
import com.example.foodeli.Fragments.Order.IdToSerialString;
import com.example.foodeli.Fragments.Shop.OnMethodShopManage;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportDate;
import com.example.foodeli.Supports.SupportImage;
import com.example.foodeli.Supports.SupportState;

import java.util.ArrayList;

public class ShopOrderAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<OrderWithState> list;
    private SupportImage supportImage = new SupportImage();
    private TextView oId, oStatus, oItems, oPaid, oUpdate, oTotal;
    private ImageView oIcon, oTotalIcon;
    private RelativeLayout oIconLayout;
    private AppCompatButton oMethodBtn;
    private IdToSerialString idToSerialString;
    private SupportState supportState = new SupportState();
    private OnMethodOrderShop onMethodOrderShop;
    private SupportDate supportDate = new SupportDate();

    public ShopOrderAdapter(Context context, ArrayList<OrderWithState> list) {
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
    public OrderWithState getItem(int position) {
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
        return list.get(position).getOid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_order_shop, parent, false);
        OrderWithState item = getItem(position);
        idToSerialString = new IdToSerialString();

        oIcon= convertView.findViewById(R.id.item_shop_list_order_icon);
        oIconLayout = convertView.findViewById(R.id.item_shop_list_order_icon_layout);
        oId = convertView.findViewById(R.id.item_shop_list_order_id);
        oStatus = convertView.findViewById(R.id.item_shop_list_order_status);
        oItems = convertView.findViewById(R.id.item_shop_list_order_item_count);
        oPaid = convertView.findViewById(R.id.item_shop_list_order_payed);
        oTotal = convertView.findViewById(R.id.item_shop_list_order_total);
        oTotalIcon = convertView.findViewById(R.id.item_shop_list_order_total_icon);
        oUpdate = convertView.findViewById(R.id.item_shop_list_order_update);
        oMethodBtn = convertView.findViewById(R.id.item_shop_list_order_method);

        oId.setText(idToSerialString.convertIdToSerialString(item.getOid()));
        oStatus.setText(item.getState());
        oItems.setText(item.getItemCount() + (item.getItemCount() > 1 ? " Items" : " Item"));
        oIcon.setImageResource(supportState.convertIdStateToIcon(item.getStateId()));
        oPaid.setText(item.getPayed() == 0 ? "Unpaid" : "Paid");
        oTotal.setText(String.valueOf(item.getTotal()));
        oUpdate.setText("Update: " + supportDate.ConvertLAtoVN(item.getModified()));

        if(item.getStateId()!=6) {
            oId.setTextColor(context.getColor(R.color.green_100));
            oIcon.setColorFilter(context.getColor(R.color.green_100));
            oIconLayout.setBackgroundResource(R.drawable.custom_button_style_sec);
            oTotal.setTextColor(context.getColor(R.color.green_100));
            oTotalIcon.setColorFilter(context.getColor(R.color.green_100));
            if(item.getStateId() > 3) {
                oMethodBtn.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
            else {
                ConvertIdToMethodBtn(item.getStateId(), oMethodBtn);
                oMethodBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onMethodOrderShop.onChangeStated(position);
                    }
                });
            }
        }
        else {
            oId.setTextColor(context.getColor(R.color.orange_400));
            oIcon.setColorFilter(context.getColor(R.color.orange_400));
            oIconLayout.setBackgroundResource(R.drawable.custome_button_style_third);
            oMethodBtn.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            oTotal.setTextColor(context.getColor(R.color.orange_400));
            oTotalIcon.setColorFilter(context.getColor(R.color.orange_400));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetailIntent = new Intent(context, OrderDetailActivity.class);
                orderDetailIntent.putExtra("oid", item.getOid());
                context.startActivity(orderDetailIntent);
            }
        });

        return convertView;
    }

    public interface OnMethodOrderShop {


        void onChangeStated(int position);
    }

    private void ConvertIdToMethodBtn(int id, AppCompatButton onMethodBtn) {
        switch(id) {
            case 1:
                onMethodBtn.setText("Cook it!");
                break;
            case 2:
                onMethodBtn.setText("Deliver");
                break;
            case 3:
                onMethodBtn.setText("Arrived");
                break;
            default:
                break;
        }
    }

    public void setOnMethodOrderShop(OnMethodOrderShop onMethodOrderShop) {
        this.onMethodOrderShop = onMethodOrderShop;
    }

    public void setList(ArrayList<OrderWithState> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
