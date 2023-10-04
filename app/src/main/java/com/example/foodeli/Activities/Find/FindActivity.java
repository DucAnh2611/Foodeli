package com.example.foodeli.Activities.Find;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FindActivity extends AppCompatActivity{
    private int MAX_ITEM_IN_HISTORY = 10, SEARCH_TYPE = 0;
    private SharedPreferences historyPrefs;
    private SharedPreferences.Editor editor;
    private HomeViewModel homeViewModel;
    private ArrayList<String> histories;
    private ListView listView;
    private ImageButton filterOpen, searchIcon;
    private Button showAll;
    private EditText keySearch;
    private HistoryFindAdapter adapter;
    private Filter filterFind;
    private LinearLayout historyLay;
    private boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        filterFind = new Filter();

        historyPrefs = getSharedPreferences("UserInfo", MODE_PRIVATE);

        listView = findViewById(R.id.find_listview_history);
        historyLay = findViewById(R.id.find_history_layout);
        showAll = findViewById(R.id.find_show_all);
        keySearch = findViewById(R.id.find_key);
        searchIcon = findViewById(R.id.find_icon);
        filterOpen = findViewById(R.id.find_filter_btn);

        getHistories();

        keySearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    SearchByFilter();
                    return true;
                }
                return false;
            }
        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchByFilter();
            }
        });
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show = !show;
                if(show) {
                    showAll.setText("Remove All");
                }
                else {
                    removeHistories();
                    showAll.setText("Show more");
                }
                getHistories();
            }
        });

        homeViewModel.getListAllCategory().observe(this, new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                filterOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFilter(categories);
                    }
                });
            }
        });

    }

    private void SearchByFilter() {
        Intent findProductIntent = new Intent(this, FindResultActivity.class);

        String key = keySearch.getText().toString();

        if (histories.size() > MAX_ITEM_IN_HISTORY) {
            histories.remove(0);
        }
        histories.remove(key);
        histories.add(0, key);

        filterFind.setKey(keySearch.getText().toString());

        findProductIntent.putExtra("key", filterFind.getKey());
        findProductIntent.putExtra("min", filterFind.getMin());
        findProductIntent.putExtra("max", filterFind.getMax());
        findProductIntent.putExtra("cid", filterFind.getCid());
        findProductIntent.putExtra("page", filterFind.getPage());

        Gson gson = new Gson();
        String json = gson.toJson(histories);
        editor = historyPrefs.edit();
        editor.putString("histories", json);
        editor.apply();

        startActivity(findProductIntent);

        getHistories();
    }

    private void getHistories() {
        String json = historyPrefs.getString("histories", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        histories = gson.fromJson(json, type);

        if(histories!=null) {
            adapter = new HistoryFindAdapter( show? histories : new ArrayList<>(histories.subList(0, Math.min(5, histories.size()))) , this);
            adapter.setOnUpdateFindData(new OnUpdateFindData() {

                @Override public void onClickHistory() {
                    keySearch.setText(adapter.getKey());
                    SearchByFilter();
                }

                @Override public void onRemoveHistory() {
                    histories.remove(adapter.getDeleKey());

                    Gson gson = new Gson();
                    String json = gson.toJson(histories);
                    editor = historyPrefs.edit();
                    editor.putString("histories", json);
                    editor.apply();

                    getHistories();
                }

                @Override
                public void onChangeMin() {

                }

                @Override
                public void onChangeMax() {

                }

                @Override
                public void onClickCategory() {

                }
            });
            listView.setAdapter(adapter);
            if(histories.size() <= 5 && !show) showAll.setVisibility(View.INVISIBLE);
            else showAll.setVisibility(View.VISIBLE);
        }
        else {
            histories = new ArrayList<>();
            historyLay.removeView(showAll);
        }
    }

    private void removeHistories() {
        histories = new ArrayList<>();

        Gson gson = new Gson();
        String json = gson.toJson(histories);
        editor = historyPrefs.edit();
        editor.putString("histories", json);
        editor.apply();

        getHistories();
    }

    private void showFilter(ArrayList<Category> categories) {
        FilterScreenDialog dialog = new FilterScreenDialog(categories, filterFind, this);
        dialog.setOnApplyFilter(new FilterScreenDialog.OnApplyFilter() {
            @Override
            public void onApplyFilter(int cid, float min, float max) {
                filterFind.setCid(cid);
                filterFind.setMax(max);
                filterFind.setMin(min);

                SearchByFilter();
            }
        });
        dialog.show(getSupportFragmentManager(), "fullscreen_dialog_find_filter");
    }

}