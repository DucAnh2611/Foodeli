package com.example.foodeli.Fragments.ShopVoucher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.foodeli.Activities.ShopManage.ShopManageViewModel;
import com.example.foodeli.Fragments.Shop.OnMethodShopManage;
import com.example.foodeli.Fragments.ShopProduct.DynamicHeightGridView;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Body.VoucherShopBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response.CreateVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response.DeleteVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllUserInShop;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
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
 * Use the {@link ShopVoucher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopVoucher extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SID = "arg_sid";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int sid;
    private AppCompatButton newVoucherBtn;
    private View view;
    private GridView voucherGV;
    private ShopManageViewModel shopManageViewModel;
    private ShopVoucherAdapter adapter;
    private User user;
    private DialogCreateVoucher dialog;

    public ShopVoucher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sid ShopId.
     * @return A new instance of fragment ShopVoucher.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopVoucher newInstance(int sid) {
        ShopVoucher fragment = new ShopVoucher();
        Bundle args = new Bundle();
        args.putInt(ARG_SID, sid);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_voucher, container, false);
        sid = getArguments().getInt(ARG_SID);

        shopManageViewModel = new ViewModelProvider(getActivity()).get(ShopManageViewModel.class);
        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        newVoucherBtn = view.findViewById(R.id.item_shop_voucher_create);
        voucherGV = view.findViewById(R.id.item_shop_voucher_gridview);

        adapter = new ShopVoucherAdapter(getContext(), new ArrayList<>());
        voucherGV.setAdapter(adapter);

        shopManageViewModel.getListVoucherShop(sid).observe(getViewLifecycleOwner(), new Observer<ArrayList<Voucher>>() {
            @Override
            public void onChanged(ArrayList<Voucher> vouchers) {
                adapter = new ShopVoucherAdapter(getContext(), vouchers);
                voucherGV.setAdapter(adapter);

                adapter.setOnMethodShopManage(new OnMethodShopManage() {
                    @Override
                    public void onEdit(int position, Object obj) {
                        // Not use
                    }

                    @Override
                    public void onDelete(int position, int id) {
                        deleteVoucher(id, user.getId(), position);
                    }
                });
            }
        });

        newVoucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFormCreateVoucher();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void ShowFormCreateVoucher() {
        dialog = new DialogCreateVoucher(getContext());
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_create_voucher");
        dialog.setOnSelectMethodVoucher(new DialogCreateVoucher.OnSelectMethodVoucher() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onConfirm(String code, String title, String desc, float discount,
                                  float min, float max, int limit, String target, int expire) {
                VoucherShopBody body = new VoucherShopBody(user.getId(), sid, expire, limit, "",
                        title, desc, code, target, min, max, discount);

                if(body.checkIfValid().isEmpty()) {
                    createVoucher(body);
                }
                else {
                    System.out.println(body);
                    Toast.makeText(getContext(), "Some field is not valid (empty or blank)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void deleteVoucher(int vid, int uid, int position) {
        Pool pool= new Pool();

        Call<DeleteVoucherRes> deleteVoucherResCall = pool.getApiCallShopVoucher().deleteVoucher(uid, vid, sid);

        deleteVoucherResCall.enqueue(new Callback<DeleteVoucherRes>() {
            @Override
            public void onResponse(Call<DeleteVoucherRes> call, Response<DeleteVoucherRes> response) {
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
                    ArrayList<Voucher> oldList = shopManageViewModel.getListVoucherShop(sid).getValue();
                    oldList.remove(position);

                    shopManageViewModel.setListVoucherShop(oldList);
                }
            }

            @Override
            public void onFailure(Call<DeleteVoucherRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void createVoucher(VoucherShopBody body) {
        Pool pool = new Pool();

        Call<CreateVoucherRes> createVoucher = pool.getApiCallShopVoucher().createVoucher(body);

        createVoucher.enqueue(new Callback<CreateVoucherRes>() {
            @Override
            public void onResponse(Call<CreateVoucherRes> call, Response<CreateVoucherRes> response) {
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
                    ArrayList<Voucher> oldList = shopManageViewModel.getListVoucherShop(sid).getValue();
                    oldList.add(response.body().getVoucher());
                    dialog.dismiss();
                    shopManageViewModel.setListVoucherShop(oldList);
                }
            }

            @Override
            public void onFailure(Call<CreateVoucherRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}