package com.example.foodeli.Activities.MethodCheckout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodeli.Activities.SelectPayment.SelectPaymentUserMethodAdapter;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.MethodSupport;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.CreateMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.example.foodeli.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SystemPaymentAdapter extends BaseAdapter {

    private Context context;
    private HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod;
    private ArrayList<MethodSupport> listSupport;
    private HashMap<String, Integer> mapIconSupportList;
    private ImageView iconMethod, iconOpen;
    private TextView contentMethod;
    private LinearLayout methodLayout;
    private GridView subMethodGV;
    private int methodSelectToView;
    private UserPaymentAdapter adapter;

    public SystemPaymentAdapter(ArrayList<MethodSupport> listSupport,
                                HashMap<String, Integer> mapIconSupportList,
                                HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod,
                                Context context){
        this.listSupport = listSupport;
        this.mapIconSupportList = mapIconSupportList;
        this.mapListMethod = mapListMethod;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listSupport.size();
    }

    @Override
    public MethodSupport getItem(int position) {
        if(listSupport.isEmpty()) {
            return null;
        }
        return listSupport.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (listSupport.isEmpty()) {
            return 0;
        }
        return listSupport.get(position).getMid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_user_payment_gridview, parent, false);
        }

        MethodSupport methodSupport = getItem(position);

        iconMethod = convertView.findViewById(R.id.user_method_support_icon);
        iconOpen = convertView.findViewById(R.id.user_method_support_is_select);
        contentMethod = convertView.findViewById(R.id.user_method_support_name);
        methodLayout = convertView.findViewById(R.id.user_method_payment_layout);
        subMethodGV = convertView.findViewById(R.id.user_method_support_subgridview);

        if(mapListMethod != null && !mapListMethod.isEmpty() ) {
            iconMethod.setImageResource(mapIconSupportList.get(methodSupport.getType()));
            contentMethod.setText(methodSupport.getType());

            if (methodSupport.getType().equals("Cash")) {
                iconOpen.setVisibility(View.INVISIBLE);
                subMethodGV.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            }
            else {
                iconOpen.setVisibility(View.VISIBLE);
                if (methodSelectToView == methodSupport.getMid()) {
                    if (mapListMethod.get(methodSupport.getType()) != null) {

                        adapter = new UserPaymentAdapter(mapListMethod.get(methodSupport.getType()), context);
                        subMethodGV.setAdapter(adapter);

                        int desiredHeight = (int) (mapListMethod.get(methodSupport.getType()).size() * (context.getResources().getDimension(R.dimen.item_in_method) + context.getResources().getDimension(R.dimen.item_gap_in_method)) + subMethodGV.getPaddingTop() + subMethodGV.getPaddingBottom());
                        subMethodGV.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                desiredHeight
                        ));

                    }

                    iconOpen.setImageResource(R.drawable.down_arrow);
                }
                else {
                    iconOpen.setImageResource(R.drawable.right_arrow);
                    subMethodGV.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    methodLayout.setBackgroundResource(R.drawable.custom_input_style);
                }
                methodLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (methodSelectToView == methodSupport.getMid()) {
                            methodSelectToView = 0;
                        } else {
                            methodSelectToView = methodSupport.getMid();
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }
        return convertView;
    }

    public void setMapListMethod(HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod) {
        this.mapListMethod = mapListMethod;
        notifyDataSetChanged();
    }
}
