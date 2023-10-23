package com.example.foodeli.Activities.ShopManage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.ShopPermission;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.CategoryRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.ShopPermissionRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response.GetAllVoucherShopRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllOrderShopRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllProductShop;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllUserInShop;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetShopInformationResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopManageViewModel extends ViewModel {
    private Pool pool;
    private MutableLiveData<ArrayList<GetAllUserInShop.UserInShop>> listUserInShop;
    private MutableLiveData<GetAllShopUserHaveResponse.ShopWithDetail> shopInfo;
    private MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>> listProductInShop;
    private MutableLiveData<ArrayList<OrderWithState>> listOrderShop;
    private MutableLiveData<ArrayList<Voucher>> listVoucherShop;
    private MutableLiveData<ArrayList<ShopPermission>> listPermissions;
    private MutableLiveData<ArrayList<Category>> listCategory;

    // GETTER
    public MutableLiveData<ArrayList<GetAllUserInShop.UserInShop>> getListUserInShop(int sid) {
        if(listUserInShop== null) {
            listUserInShop = new MutableLiveData<ArrayList<GetAllUserInShop.UserInShop>>();
            loadUserInShop(sid);
        }
        return listUserInShop;
    }

    public MutableLiveData<GetAllShopUserHaveResponse.ShopWithDetail> getShopInfo(int sid) {
        if(shopInfo == null) {
            shopInfo = new MutableLiveData<GetAllShopUserHaveResponse.ShopWithDetail>();
            loadShopInfo(sid);
        }
        return shopInfo;
    }

    public MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>> getListProductInShop(int sid) {
        if(listProductInShop == null) {
            listProductInShop = new MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>>();
            loadProductInShop(sid);
        }
        return listProductInShop;
    }

    public MutableLiveData<ArrayList<OrderWithState>> getListOrderShop(int sid) {
        if(listOrderShop == null) {
            listOrderShop = new MutableLiveData<ArrayList<OrderWithState>>();
            loadOrderInShop(sid);
        }
        return listOrderShop;
    }

    public MutableLiveData<ArrayList<Voucher>> getListVoucherShop(int sid) {
        if(listVoucherShop == null) {
            listVoucherShop = new MutableLiveData<ArrayList<Voucher>>();
            loadVoucherInShop(sid);
        }
        return listVoucherShop;
    }

    public MutableLiveData<ArrayList<Category>> getListCategory() {
        if(listCategory == null) {
            listCategory = new MutableLiveData<ArrayList<Category>>();
            loadCategories();
        }
        return listCategory;
    }

    public MutableLiveData<ArrayList<ShopPermission>> getListPermissions() {
        if(listPermissions == null) {
            listPermissions = new MutableLiveData<ArrayList<ShopPermission>>();
            loadShopPermission();
        }
        return listPermissions;
    }

    // SETTER
    public void setShopInfo(GetAllShopUserHaveResponse.ShopWithDetail shopInfo) {
        if(this.shopInfo == null) {
            this.shopInfo = new MutableLiveData<GetAllShopUserHaveResponse.ShopWithDetail>();
        }
        this.shopInfo.setValue(shopInfo);
    }

    public void setListProductInShop(ArrayList<GetTopProduct.ProductWithAvg> listProductInShop) {
        this.listProductInShop.setValue(listProductInShop);
    }

    public void setListUserInShop(ArrayList<GetAllUserInShop.UserInShop> listUserInShop) {
        this.listUserInShop.setValue(listUserInShop);
    }

    public void setListVoucherShop(ArrayList<Voucher> listVoucherShop) {
        this.listVoucherShop.setValue(listVoucherShop);
    }

    public void setListOrderShop(ArrayList<OrderWithState> listOrderShop) {
        this.listOrderShop.setValue(listOrderShop);
    }

    //LOADER
    private void loadShopInfo(int sid) {
        pool = new Pool();

        Call<GetShopInformationResponse> getInfoShop = pool.getApiCallUserShop().getShop(sid);
        getInfoShop.enqueue(new Callback<GetShopInformationResponse>() {
            @Override
            public void onResponse(Call<GetShopInformationResponse> call, Response<GetShopInformationResponse> response) {
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
                    shopInfo.setValue(response.body().getInfo());
                }
            }

            @Override
            public void onFailure(Call<GetShopInformationResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void loadUserInShop(int sid) {
        pool = new Pool();

        Call<GetAllUserInShop> getAllUserInShopCall = pool.getApiCallUserShop().getEmployee(sid);

        getAllUserInShopCall.enqueue(new Callback<GetAllUserInShop>() {
            @Override
            public void onResponse(Call<GetAllUserInShop> call, Response<GetAllUserInShop> response) {
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
                    listUserInShop.setValue(response.body().getUsers());
                }
            }

            @Override
            public void onFailure(Call<GetAllUserInShop> call, Throwable t) {

            }
        });
    }
    private void loadProductInShop(int sid) {
        pool = new Pool();

        Call<GetAllProductShop> getList = pool.getApiCallUserShop().getShopProduct(sid);
        getList.enqueue(new Callback<GetAllProductShop>() {
            @Override
            public void onResponse(Call<GetAllProductShop> call, Response<GetAllProductShop> response) {
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
                    listProductInShop.setValue(response.body().getProducts());
                }
            }

            @Override
            public void onFailure(Call<GetAllProductShop> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void loadVoucherInShop(int sid) {
        pool = new Pool();

        Call<GetAllVoucherShopRes> getAll = pool.getApiCallShopVoucher().getAlVoucherShop(sid);
        getAll.enqueue(new Callback<GetAllVoucherShopRes>() {
            @Override
            public void onResponse(Call<GetAllVoucherShopRes> call, Response<GetAllVoucherShopRes> response) {
                if (response.code() != 200) {
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
                    listVoucherShop.setValue(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<GetAllVoucherShopRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void loadOrderInShop(int sid) {
        pool = new Pool();

        Call<GetAllOrderShopRes> getAll = pool.getApiCallUserShop().getShopOrder(sid);
        getAll.enqueue(new Callback<GetAllOrderShopRes>() {
            @Override
            public void onResponse(Call<GetAllOrderShopRes> call, Response<GetAllOrderShopRes> response) {
                if (response.code() != 200) {
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
                    listOrderShop.setValue(response.body().getOrders());
                }
            }

            @Override
            public void onFailure(Call<GetAllOrderShopRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    private void loadCategories() {
        pool = new Pool();
        Call<CategoryRes> getCategories = pool.getApiCallGeneral().getSystemCategory(0);

        getCategories.enqueue(new Callback<CategoryRes>() {
            @Override
            public void onResponse(Call<CategoryRes> call, Response<CategoryRes> responseC) {
                if (!responseC.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(responseC.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    listCategory.setValue(responseC.body().getCategories());
                }
            }

            @Override
            public void onFailure(Call<CategoryRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadShopPermission() {
        pool = new Pool();
        Call<ShopPermissionRes> getPermissions = pool.getApiCallGeneral().getSystemShopPermission();

        getPermissions.enqueue(new Callback<ShopPermissionRes>() {
            @Override
            public void onResponse(Call<ShopPermissionRes> call, Response<ShopPermissionRes> responseC) {
                if (!responseC.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(responseC.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    listPermissions.setValue(responseC.body().getPermissions());
                }
            }

            @Override
            public void onFailure(Call<ShopPermissionRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

}
