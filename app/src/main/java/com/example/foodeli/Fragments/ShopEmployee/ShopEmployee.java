package com.example.foodeli.Fragments.ShopEmployee;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.foodeli.Activities.ShopManage.ShopManageViewModel;
import com.example.foodeli.Fragments.Profile.MenuSelectImage;
import com.example.foodeli.Fragments.Profile.OnSelectMenuPicture;
import com.example.foodeli.Fragments.Shop.OnMethodShopManage;
import com.example.foodeli.Fragments.ShopOrder.ShopOrder;
import com.example.foodeli.Fragments.ShopProduct.DynamicHeightGridView;
import com.example.foodeli.Fragments.ShopVoucher.ShopVoucherAdapter;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.ShopPermission;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body.ManagerUserBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllUserInShop;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.MangeUserRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopEmployee#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopEmployee extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SID = "arg_sid";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ShopManageViewModel shopManageViewModel;
    private AppCompatButton addEmp;
    private GridView empGridView;
    private ShopEmployeeAdapter adapter;
    private int sid;
    private User user;
    private DialogShopUser dialog;
    private DialogShopUserForm formEdit;
    private ArrayList<ShopPermission> shopPermissions;
    private ShopOrder.OnAdjustOrder onAdjustOrder;

    public ShopEmployee() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sid shopId .
     * @return A new instance of fragment ShopEmployee.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopEmployee newInstance(int sid) {
        ShopEmployee fragment = new ShopEmployee();
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
        View view = inflater.inflate(R.layout.fragment_shop_employee, container, false);
        sid = getArguments().getInt(ARG_SID);

        shopManageViewModel = new ViewModelProvider(getActivity()).get(ShopManageViewModel.class);
        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        addEmp = view.findViewById(R.id.item_shop_employee_create);
        empGridView = view.findViewById(R.id.item_shop_employee_gridview);

        shopManageViewModel.getListPermissions().observe(getViewLifecycleOwner(), new Observer<ArrayList<ShopPermission>>() {
            @Override
            public void onChanged(ArrayList<ShopPermission> list) {
                shopPermissions = new ArrayList<>();

                for (int i = 1; i < list.size(); i++) {
                    shopPermissions.add(list.get(i));
                }
            }
        });

        shopManageViewModel.getListUserInShop(sid).observe(getViewLifecycleOwner(), new Observer<ArrayList<GetAllUserInShop.UserInShop>>() {
            @Override
            public void onChanged(ArrayList<GetAllUserInShop.UserInShop> userInShops) {
                adapter = new ShopEmployeeAdapter(getContext(), userInShops);
                empGridView.setAdapter(adapter);

                adapter.setOnMethodShopManage(new OnMethodShopManage() {
                    @Override
                    public void onEdit(int position, Object obj) {
                        showFormEdit(position, (GetAllUserInShop.UserInShop) obj);
                    }

                    @Override
                    public void onDelete(int position, int id) {
                        removeUserFromShop(id, position);
                    }
                });
            }
        });

        addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFindUser();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showFormEdit(int position, GetAllUserInShop.UserInShop user) {
        formEdit = new DialogShopUserForm(getContext(),shopPermissions, user);
        formEdit.show(getActivity().getSupportFragmentManager(), "dialog_shop_edit_user");
        formEdit.setOnSelectMethodEditUser(new DialogShopUserForm.OnSelectMethodEditUser() {
            @Override
            public void onCancel() {
                formEdit.dismiss();
            }

            @Override
            public void onEdit(int pmid, String role) {
                updateUser(user, position, pmid, role);
            }
        });
    }
    private void showDialogFindUser() {
        dialog = new DialogShopUser(getContext());
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_shop_find_user");
        dialog.setOnAddUserToShop(new UserFoundAdapter.OnAddUserToShop() {
            @Override
            public void onAddUser(User user) {
                addUserToShop(user);
            }
        });
    }
    private void addUserToShop(User user_add) {

        Pool pool = new Pool();

        ManagerUserBody body = new ManagerUserBody(user.getId(), user_add.getId(), sid, 2);

        Call<MangeUserRes> addUser = pool.getApiCallUserShop().createEmployee(body);
        addUser.enqueue(new Callback<MangeUserRes>() {
            @Override
            public void onResponse(Call<MangeUserRes> call, Response<MangeUserRes> response) {
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
                    ArrayList<GetAllUserInShop.UserInShop> oldList = shopManageViewModel.getListUserInShop(sid).getValue();


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date currentTime = new Date();
                    String formattedDateTime = dateFormat.format(currentTime);

                    GetAllUserInShop.UserInShop newUser = new GetAllUserInShop.UserInShop(user_add, "Employee", formattedDateTime);
                    oldList.add(newUser);

                    shopManageViewModel.setListUserInShop(oldList);
                }
            }

            @Override
            public void onFailure(Call<MangeUserRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void updateUser(GetAllUserInShop.UserInShop user_add, int position, int osid, String role) {

        Pool pool = new Pool();

        Call<MangeUserRes> updateUser = pool.getApiCallUserShop().updateEmployeePermission(user.getId(), user_add.getId(), sid, osid);
        updateUser.enqueue(new Callback<MangeUserRes>() {
            @Override
            public void onResponse(Call<MangeUserRes> call, Response<MangeUserRes> response) {
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
                    formEdit.dismiss();
                    ArrayList<GetAllUserInShop.UserInShop> oldList = shopManageViewModel.getListUserInShop(sid).getValue();
                    oldList.remove(position);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date currentTime = new Date();
                    String formattedDateTime = dateFormat.format(currentTime);

                    user_add.setRole(role);
                    user_add.setUpdate(formattedDateTime);
                    oldList.add(user_add);

                    shopManageViewModel.setListUserInShop(oldList);
                    onAdjustOrder.onAdjust();
                }
            }

            @Override
            public void onFailure(Call<MangeUserRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void removeUserFromShop(int uid, int position) {

        Pool pool = new Pool();

        Call<MangeUserRes> removeUser = pool.getApiCallUserShop().deleteEmployee(user.getId(), uid, sid);
        removeUser.enqueue(new Callback<MangeUserRes>() {
            @Override
            public void onResponse(Call<MangeUserRes> call, Response<MangeUserRes> response) {
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
                    ArrayList<GetAllUserInShop.UserInShop> oldList = shopManageViewModel.getListUserInShop(sid).getValue();
                    oldList.remove(position);

                    shopManageViewModel.setListUserInShop(oldList);
                }
            }

            @Override
            public void onFailure(Call<MangeUserRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setOnAdjustOrder(ShopOrder.OnAdjustOrder onAdjustOrder) {
        this.onAdjustOrder = onAdjustOrder;
    }
}