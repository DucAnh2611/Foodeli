package com.example.foodeli.Fragments.ShopProduct;

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
import com.example.foodeli.Fragments.ShopEmployee.ShopEmployeeAdapter;
import com.example.foodeli.Fragments.ShopVoucher.ShopVoucherAdapter;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body.UpdateProductBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.CreateProductRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.DeleteInfoRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopProduct extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SID = "arg_sid";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ShopManageViewModel shopManageViewModel;
    private View view;
    private int sid;
    private GridView gridviewProduct;
    private AppCompatButton addNewProduct;
    private ShopProductAdapter adapter;
    private DialogProductForm dialog;
    private User user;
    private ArrayList<Category> categories;
    private ArrayList<GetTopProduct.ProductWithAvg> shopProducts;
    private SupportDate supportDate = new SupportDate();
    private OnCreateNewProduct onCreateNewProduct;

    public ShopProduct() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sid shopId .
     * @return A new instance of fragment ShopProduct.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopProduct newInstance(int sid) {
        ShopProduct fragment = new ShopProduct();
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
        View view = inflater.inflate(R.layout.fragment_shop_product, container, false);

        shopManageViewModel = new ViewModelProvider(getActivity()).get(ShopManageViewModel.class);
        sid = getArguments().getInt(ARG_SID);
        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        addNewProduct = view.findViewById(R.id.item_shop_product_create);
        gridviewProduct = view.findViewById(R.id.item_shop_product_gridview);

        adapter = new ShopProductAdapter(getContext(), new ArrayList<>());
        gridviewProduct.setAdapter(adapter);

        shopManageViewModel.getListProductInShop(sid).observe(getViewLifecycleOwner(), new Observer<ArrayList<GetTopProduct.ProductWithAvg>>() {
            @Override
            public void onChanged(ArrayList<GetTopProduct.ProductWithAvg> productWithAvgs) {
                shopProducts = productWithAvgs;

                shopManageViewModel.getListCategory().observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
                    @Override
                    public void onChanged(ArrayList<Category> list) {
                        adapter = new ShopProductAdapter(getContext(), productWithAvgs);
                        gridviewProduct.setAdapter(adapter);
                        adapter.setOnMethodShopManage(new OnMethodShopManage() {
                            @Override
                            public void onEdit(int position, Object obj) {
                                ShowDialogEditProduct( ((GetTopProduct.ProductWithAvg) obj).getPid(), position);
                            }

                            @Override
                            public void onDelete(int position, int id) {
                                DeleteProduct(position, id);
                            }
                        });

                        categories = list;
                        addNewProduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShowDialogEditProduct();
                            }
                        });

                    }
                });

            }
        });

        return view;
    }

    private void ShowDialogEditProduct() {
        dialog = new DialogProductForm(getContext(), categories);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_create_product");
        dialog.setOnMethodProductForm(new DialogProductForm.OnMethodProductForm() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onConfirm(UpdateProductBody body, int pid, String newImage) {
                body.setSid(sid);
                body.setUid(user.getId());

                if(body.checkIfValid().isEmpty()) CreateProduct(body);
                else {
                    Toast.makeText(getContext(), "All field is required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void ShowDialogEditProduct(int pid, int position) {
        dialog = new DialogProductForm(getContext(), pid, categories);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_create_product");
        dialog.setOnMethodProductForm(new DialogProductForm.OnMethodProductForm() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onConfirm(UpdateProductBody body, int pid, String newImage) {
                body.setSid(sid);
                body.setUid(user.getId());

                if(body.checkIfValid().isEmpty()) UpdateFormProduct(body, pid, position, newImage);
                else {
                    Toast.makeText(getContext(), "All field is required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    private void UpdateFormProduct(UpdateProductBody body, int pid, int position, String newImage) {
        Pool pool = new Pool();

        Call<CreateProductRes> processForm = pool.getApiCallShopProduct().updateProductData(body, pid);
        processForm.enqueue(new Callback<CreateProductRes>() {
            @Override
            public void onResponse(Call<CreateProductRes> call, Response<CreateProductRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {

                    GetTopProduct.ProductWithAvg newProduct = shopProducts.get(position);
                    newProduct.setName(body.getName());
                    newProduct.setPrice(body.getPrice());
                    newProduct.setUnit(body.getUnit());
                    newProduct.setModified(supportDate.GetCurrentDateVN());
                    newProduct.setPimage(newImage);

                    shopProducts.set(position, newProduct);
                    adapter.setList(shopProducts);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CreateProductRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }
    private void CreateProduct(UpdateProductBody body) {
        Pool pool = new Pool();

        Call<CreateProductRes> processForm = pool.getApiCallShopProduct().newProduct(body);
        processForm.enqueue(new Callback<CreateProductRes>() {
            @Override
            public void onResponse(Call<CreateProductRes> call, Response<CreateProductRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    CreateProductRes.P product = response.body().getProduct();

                    GetTopProduct.ProductWithAvg newProduct = new GetTopProduct.ProductWithAvg();
                    newProduct.setName(product.getProduct().getName());
                    newProduct.setPrice(product.getProduct().getPrice());
                    newProduct.setPid(product.getProduct().getPid());
                    newProduct.setUnit(product.getProduct().getUnit());
                    newProduct.setStock(product.getProduct().getStock());
                    newProduct.setModified(product.getProduct().getModified());
                    newProduct.setPimage(product.getImages().get(0).getBase64());

                    shopProducts.add(newProduct);
                    adapter.setList(shopProducts);
                    dialog.dismiss();
                    onCreateNewProduct.onCreate();
                }
            }

            @Override
            public void onFailure(Call<CreateProductRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }
    private void DeleteProduct(int position, int pid) {
        Pool pool = new Pool();

        Call<DeleteInfoRes> deleteProduct = pool.getApiCallShopProduct().deleteProduct(user.getId(), sid, pid);
        deleteProduct.enqueue(new Callback<DeleteInfoRes>() {
            @Override
            public void onResponse(Call<DeleteInfoRes> call, Response<DeleteInfoRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    shopProducts.remove(position);
                    adapter.setList(shopProducts);
                    onCreateNewProduct.onDelete();
                }
            }

            @Override
            public void onFailure(Call<DeleteInfoRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }
    public void setSid(int sid) {
        this.sid = sid;
    }

    public interface OnCreateNewProduct {
        void onCreate();
        void onDelete();
    }

    public void setOnCreateNewProduct(OnCreateNewProduct onCreateNewProduct) {
        this.onCreateNewProduct = onCreateNewProduct;
    }
}