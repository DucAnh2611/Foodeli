package com.example.foodeli.Activities.OrderStatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.General.Body.CancelReason;
import com.example.foodeli.R;

import java.util.ArrayList;

public class CancelReasonAdapter extends BaseAdapter {

    private ArrayList<CancelReason> reasons;
    private Context context;
    private int currId = 0;
    private OnClickReason onClickReason;
    private View inner;
    private TextView content;
    private RelativeLayout selectLayout;

    public CancelReasonAdapter(ArrayList<CancelReason> reasons, int currId, Context context) {
        this.reasons = reasons;
        this.context = context;
        this.currId = currId;
    }

    @Override
    public int getCount() {
        return reasons.size();
    }

    @Override
    public CancelReason getItem(int position) {
        if(reasons.isEmpty()) {
            return null;
        }
        return reasons.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(reasons.isEmpty()) {
            return 0;
        }
        return reasons.get(position).getRcId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_gridview_cancelorder_reason, parent, false);
        }

        CancelReason item = getItem(position);

        inner = convertView.findViewById(R.id.cancel_reason_inner);
        content = convertView.findViewById(R.id.cancel_reason_text);
        selectLayout = convertView.findViewById(R.id.cancel_reason_select_layout);

        content.setText(item.getRcName());

        if(currId == item.getRcId()) {
            inner.setVisibility(View.VISIBLE);
            convertView.setBackgroundResource(R.drawable.custom_button_style_sec);
        }
        else {
            inner.setVisibility(View.INVISIBLE);
            convertView.setBackgroundResource(R.color.white);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currId = item.getRcId();
                onClickReason.onClickReason();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public interface OnClickReason {
        void onClickReason();
    }

    public void setOnItemClickReason(OnClickReason listener) {
        this.onClickReason = listener;
    }

    public int getCurrId() {
        return currId;
    }
}
