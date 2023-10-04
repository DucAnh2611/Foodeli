package com.example.foodeli.Activities.Find;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodeli.R;

import java.util.ArrayList;

public class HistoryFindAdapter extends BaseAdapter {

    private ArrayList<String> histories;
    private Context context;
    private TextView content;
    private ImageButton removeBtn;
    private SharedPreferences historyPrefs;
    private SharedPreferences.Editor editor;
    private OnUpdateFindData onUpdateFindData;
    private String key, deleKey;

    public HistoryFindAdapter(ArrayList<String> histories, Context context){
        this.histories = histories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public String getItem(int position) {
        return histories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_listview_find_history, parent, false);
        }

        historyPrefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = historyPrefs.edit();

        content = convertView.findViewById(R.id.find_history_content);
        removeBtn = convertView.findViewById(R.id.find_history_remove);

        content.setText(getItem(position));

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleKey = getItem(position);
                onUpdateFindData.onRemoveHistory();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = getItem(position);
                onUpdateFindData.onClickHistory();
            }
        });

        return convertView;
    }

    public String getKey() {
        return key;
    }

    public String getDeleKey() {
        return deleKey;
    }

    public void setOnUpdateFindData(OnUpdateFindData onUpdateFindData) {this.onUpdateFindData= onUpdateFindData;}
}
