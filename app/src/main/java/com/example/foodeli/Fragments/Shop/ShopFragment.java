package com.example.foodeli.Fragments.Shop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.Activities.SelectVoucher.SelectVoucherActivity;
import com.example.foodeli.Activities.ShopManage.ShopManageActivity;
import com.example.foodeli.Fragments.Profile.MenuSelectImage;
import com.example.foodeli.Fragments.Profile.OnSelectMenuPicture;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.CancelReason;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body.CreateShopBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.CreateShopResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_SHOP_MANAGE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<GetAllShopUserHaveResponse.ShopWithDetail> listShop;
    private LinearLayout gridviewLayout;
    private GridView listShopGV;
    private ImageButton addNewShop;
    private HomeViewModel homeViewModel;
    private ListShopGridviewAdapter adapter;
    private DialogCreateShop dialog;
    private Pool pool;

    private User user;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        // Inflate the layout for this fragment

        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        addNewShop = view.findViewById(R.id.shop_list_add);
        gridviewLayout = view.findViewById(R.id.shop_list_gridview_layout);
        listShopGV = view.findViewById(R.id.shop_list_gridview);

        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        homeViewModel.getListUserShop(user.getId()).observe(getViewLifecycleOwner(), new Observer<ArrayList<GetAllShopUserHaveResponse.ShopWithDetail>>() {
            @Override
            public void onChanged(ArrayList<GetAllShopUserHaveResponse.ShopWithDetail> shopWithDetails) {
                listShop = shopWithDetails;
                if(!listShop.isEmpty()) {
                    adapter = new ListShopGridviewAdapter(listShop, getContext());

                    adapter.setOnSelectShop(new ListShopGridviewAdapter.OnSelectShop() {
                        @Override
                        public void onSelectShop(GetAllShopUserHaveResponse.ShopWithDetail shop, int position) {
                            Intent shopManage = new Intent(getContext(), ShopManageActivity.class);
                            shopManage.putExtra("shop", shop);
                            shopManage.putExtra("position", position);
                            startActivityForResult(shopManage, REQUEST_SHOP_MANAGE);
                        }
                    });

                    listShopGV.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT ,
                            ViewGroup.LayoutParams.MATCH_PARENT ));
                    listShopGV.setAdapter(adapter);
                }
                else {
                    listShopGV.setLayoutParams(new LinearLayout.LayoutParams(0 ,0 ));
                    gridviewLayout.setGravity(Gravity.CENTER);

                    TextView noItem = new TextView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    noItem.setText("No Shop found");
                    noItem.setTextColor(getContext().getColor(R.color.black));
                    noItem.setTextSize(17);
                    noItem.setGravity(Gravity.CENTER);

                    ImageView noItemImage = new ImageView(getContext());
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    noItemImage.setImageResource(R.drawable.no_data);

                    gridviewLayout.addView(noItem);
                    gridviewLayout.addView(noItemImage);
                }
            }
        });

        addNewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCreateShop(user.getId());
            }
        });

        return view;
    }

    private void showDialogCreateShop(int uid) {
        dialog = new DialogCreateShop(getContext());
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_select_avatar");
        dialog.setOnCreateShopSelect(new DialogCreateShop.OnCreateShopSelect() {
            @Override
            public void onCreatedShop(String name, String desc, String address, String image) {
                CreateShopBody body = new CreateShopBody(name, desc, address, uid, image);
                createShop(body, uid);
            }

            @Override
            public void onCancelShop() {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SHOP_MANAGE && resultCode == Activity.RESULT_OK) {
            int position = data.getIntExtra("position", 0);
            GetAllShopUserHaveResponse.ShopWithDetail shop = (GetAllShopUserHaveResponse.ShopWithDetail) data.getSerializableExtra("shop");

            boolean adjustO, adjustP, adjustE;
            adjustP = data.getBooleanExtra("adjustP", false);
            adjustO = data.getBooleanExtra("adjustO", false);
            adjustE = data.getBooleanExtra("adjustE", false);

            listShop.set(position, shop);
            adapter.setList(listShop);

            if(adjustP) homeViewModel.ReloadTopProduct();
            if(adjustE) homeViewModel.ReloadShopUser(user.getId());
            if(adjustO) homeViewModel.ReloadOrderActive(user.getId());
        }

    }

    private void createShop(CreateShopBody body, int uid) {
        pool = new Pool();

        Call<CreateShopResponse> createShop = pool.getApiCallUserShop().createShop(body);
        createShop.enqueue(new Callback<CreateShopResponse>() {
            @Override
            public void onResponse(Call<CreateShopResponse> call, Response<CreateShopResponse> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                        Toast.makeText(getContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    ArrayList<GetAllShopUserHaveResponse.ShopWithDetail> oldList =  homeViewModel.getListUserShop(uid).getValue();
                    oldList.add(response.body().getShop());
                    homeViewModel.setListShop(oldList);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CreateShopResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


}