package com.example.foodeli.Activities.OrderStatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.General.Body.OrderState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportDate;

import java.util.ArrayList;

public class OrderStatusStateAdapter extends BaseAdapter {

    private ArrayList<OrderState> listState;
    private ImageView icon, next;
    private LinearLayout layout, iconLayout;
    private TextView state, dateModify;
    private Context context;
    private int currentStateId;
    private ArrayList<OrderTrackRes.OrderTimeLine> timeLine;
    private SupportDate supportDate =new SupportDate();

    public OrderStatusStateAdapter(ArrayList<OrderState> list, int currId, ArrayList<OrderTrackRes.OrderTimeLine> timeline, Context context) {
        this.listState = list;
        this.currentStateId = currId;
        this.context = context;
        this.timeLine = timeline;
    }

    @Override
    public int getCount() {
        return listState.size();
    }

    @Override
    public OrderState getItem(int position) {
        if(listState.isEmpty()) {
            return null;
        }
        return listState.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(listState.isEmpty()) {
            return 0;
        }
        return listState.get(position).getStId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_order_detail_state, parent, false);
        }

        OrderState item = getItem(position);

        iconLayout = convertView.findViewById(R.id.order_detail_state_icon_layout);
        state = convertView.findViewById(R.id.order_detail_state_text);
        icon = convertView.findViewById(R.id.order_detail_state_icon);
        next = convertView.findViewById(R.id.order_detail_state_next);
        dateModify = convertView.findViewById(R.id.order_detail_state_date);
        layout = convertView.findViewById(R.id.order_detail_state_info_layout);

        icon.setImageResource(convertIdStateToIcon(item.getStId()));
        state.setText(item.getContent());

        if(currentStateId >= item.getStId()) {
            dateModify.setText(supportDate.ConvertLAtoVN(timeLine.get(position).getUpdateAt()));
            icon.setColorFilter(context.getColor(R.color.green_100));
            next.setColorFilter(context.getColor(R.color.green_100));
            state.setTextColor(context.getColor(R.color.green_100));
        }
        else {
            if(item.getStId() == getCount()) {
                iconLayout.removeView(next);
            }
            state.setTextColor(context.getColor(R.color.grey_100));
            layout.removeView(dateModify);
        }

        return convertView;
    }

    private int convertIdStateToIcon(int id) {
        switch(id) {
            case 1:
                return R.drawable.receive_order_outline;
            case 2:
                return R.drawable.cooking_outline;
            case 3:
                return R.drawable.delivery_state;
            case 4:
                return R.drawable.arrived_outline;
            case 5:
                return R.drawable.completed_outline;
            case 6:
                return R.drawable.cancel_outline;
            default:
                return R.drawable.order_non_select;
        }
    }

}
