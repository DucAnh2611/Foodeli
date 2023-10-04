package com.example.foodeli.Activities.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Response.GetAllAddressRes;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.CancelReason;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.MethodSupport;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.OrderState;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.CancelReasonRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.CategoryRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.MethodSupportRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.OrderStateRes;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.GetAllMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Method;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.CancelRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.TrackOrderInStateRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.GetAllVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private Pool pool = null;
    private MutableLiveData<ArrayList<OrderWithState>> listOrderActive, listOrderCompleted, listOrderCancelled;
    private MutableLiveData<ArrayList<Category>> listAllCategory;
    private MutableLiveData<ArrayList<MethodSupport>> listAllMethodSupport;
    private MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>> listAllTopProduct;
    private MutableLiveData<ArrayList<GetCartRes.ProductWithImage>> listProductInCart;
    private MutableLiveData<ArrayList<GetAllVoucherRes.VoucherWithRemain>> listUserVoucher;
    private MutableLiveData<ArrayList<Address>> listUserAddress;
    private MutableLiveData<HashMap<String, ArrayList<MethodWithTypeName>>> listUserPaymentMethod;
    private MutableLiveData<ArrayList<Category>> categories;
    private MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>> topProducts;
    private MutableLiveData<ArrayList<OrderState>> listOrderState;
    private MutableLiveData<ArrayList<CancelReason>> listReason;

    public MutableLiveData<ArrayList<Address>> getListUserAddress(int uid) {
        if (this.listUserAddress == null) {
            this.listUserAddress = new MutableLiveData<ArrayList<Address>>();
            loadUserAddress(uid);
        }
        return listUserAddress;
    }

    public MutableLiveData<ArrayList<MethodSupport>> getListAllMethodSupport() {
        if(this.listAllMethodSupport == null) {
            this.listAllMethodSupport = new MutableLiveData<ArrayList<MethodSupport>>();
            loadAllMethodSupport();
        }
        return listAllMethodSupport;
    }

    public MutableLiveData<ArrayList<Category>> getListAllCategory() {
        if(this.listAllCategory == null) {
            this.listAllCategory = new MutableLiveData<ArrayList<Category>>();
            loadAllCategory();
        }
        return listAllCategory;
    }

    public MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>> getListAllTopProduct(int page) {
        if(this.listAllTopProduct == null) {
            this.listAllTopProduct = new MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>>();
            loadAllTopProduct(page);
        }
        return listAllTopProduct;
    }

    public MutableLiveData<ArrayList<GetCartRes.ProductWithImage>> getListProductInCart(int uid) {
        if(this.listProductInCart == null) {
            this.listProductInCart = new MutableLiveData<ArrayList<GetCartRes.ProductWithImage>>();
            loadCart(uid);
        }
        return listProductInCart;
    }

    public MutableLiveData<ArrayList<GetAllVoucherRes.VoucherWithRemain>> getListUserVoucher(int uid) {
        if(this.listUserVoucher == null) {
            this.listUserVoucher = new MutableLiveData<ArrayList<GetAllVoucherRes.VoucherWithRemain>>();
            loadAllUserVoucher(uid);
        }
        return listUserVoucher;
    }

    public MutableLiveData<HashMap<String, ArrayList<MethodWithTypeName>>> getListUserPaymentMethod(int uid) {
        if(this.listUserPaymentMethod == null) {
            this.listUserPaymentMethod = new MutableLiveData<HashMap<String, ArrayList<MethodWithTypeName>>>();
            loadAllUserPaymentMethod(uid);
        }
        return listUserPaymentMethod;
    }

    public MutableLiveData<ArrayList<OrderWithState>> getListOrderActive(int uid) {
        if(this.listOrderActive == null) {
            this.listOrderActive = new MutableLiveData<ArrayList<OrderWithState>>();
            loadOrderState(uid, "active");
        }
        return listOrderActive;
    }

    public MutableLiveData<ArrayList<OrderWithState>> getListOrderCompleted(int uid) {
        if(this.listOrderCompleted == null) {
            this.listOrderCompleted = new MutableLiveData<ArrayList<OrderWithState>>();
            loadOrderState(uid, "completed");
        }
        return listOrderCompleted;
    }

    public MutableLiveData<ArrayList<OrderWithState>> getListOrderCancelled(int uid) {
        if(this.listOrderCancelled == null) {
            this.listOrderCancelled = new MutableLiveData<ArrayList<OrderWithState>>();
            loadOrderState(uid, "cancelled");
        }
        return listOrderCancelled;
    }

    public LiveData<ArrayList<Category>> getCategories() {
        if (this.categories == null) {
            this.categories = new MutableLiveData<ArrayList<Category>>();
            loadCategories();
        }
        return this.categories;
    }

    public LiveData<ArrayList<GetTopProduct.ProductWithAvg>> getTopProducts() {
        if (this.topProducts == null) {
            this.topProducts = new MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>>();
            loadTopProduct();
        }
        return this.topProducts;
    }

    public LiveData<ArrayList<OrderState>> getOrderState() {
        if(this.listOrderState == null) {
            this.listOrderState = new MutableLiveData<ArrayList<OrderState>>();
            loadSystemOrderState();
        }
        return this.listOrderState;
    }

    public LiveData<ArrayList<CancelReason>> getListReason() {
        if(this.listReason == null) {
            this.listReason = new MutableLiveData<ArrayList<CancelReason>>();
            loadReasonSystem();
        }
        return this.listReason;
    }

    private void loadCategories() {
        pool = new Pool();
        Call<CategoryRes> getCategories = pool.getApiCallGeneral().getSystemCategory(6);

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
                    categories.setValue(responseC.body().getCategories());
                }
            }

            @Override
            public void onFailure(Call<CategoryRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadTopProduct() {

        pool = new Pool();
        Call<GetTopProduct> getTopProduct = pool.getApiCallGeneral().getTopProduct(1);

        getTopProduct.enqueue(new Callback<GetTopProduct>() {
            @Override
            public void onResponse(Call<GetTopProduct> call, Response<GetTopProduct> responseP) {
                if (!responseP.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(responseP.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    topProducts.setValue(responseP.body().getList());
                }
            }

            @Override
            public void onFailure(Call<GetTopProduct> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadUserAddress(int uid) {

        pool = new Pool();
        Call<GetAllAddressRes> getAllUserAdd = pool.getApiCallUserAddress().getAll(uid);

        getAllUserAdd.enqueue(new Callback<GetAllAddressRes>() {
            @Override
            public void onResponse(Call<GetAllAddressRes> call, Response<GetAllAddressRes> response) {
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
                    listUserAddress.setValue(response.body().getList());
                }
            }

            @Override
            public void onFailure(Call<GetAllAddressRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadAllCategory() {

        pool = new Pool();
        Call<CategoryRes> getAllCategory = pool.getApiCallGeneral().getSystemCategory(0);

        getAllCategory.enqueue(new Callback<CategoryRes>() {
            @Override
            public void onResponse(Call<CategoryRes> call, Response<CategoryRes> response) {
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
                    listAllCategory.setValue(response.body().getCategories());
                }
            }

            @Override
            public void onFailure(Call<CategoryRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadAllTopProduct(int page) {

        pool = new Pool();
        Call<GetTopProduct> getAllTopProduct = pool.getApiCallGeneral().getTopProduct(page);

        getAllTopProduct.enqueue(new Callback<GetTopProduct>() {
            @Override
            public void onResponse(Call<GetTopProduct> call, Response<GetTopProduct> response) {
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
                    ArrayList<GetTopProduct.ProductWithAvg> newListFromCurrentPage = listAllTopProduct.getValue();
                    newListFromCurrentPage.addAll(response.body().getList());
                    listAllTopProduct.setValue(newListFromCurrentPage);
                }
            }

            @Override
            public void onFailure(Call<GetTopProduct> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadCart(int uid) {
        pool = new Pool();
        Call<GetCartRes> getAllTopProduct = pool.getApiCallUserCart().getCartUser(uid);

        getAllTopProduct.enqueue(new Callback<GetCartRes>() {
            @Override
            public void onResponse(Call<GetCartRes> call, Response<GetCartRes> response) {
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
                    listProductInCart.setValue(response.body().getProducts());
                }
            }

            @Override
            public void onFailure(Call<GetCartRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadAllUserVoucher(int uid) {
        pool = new Pool();
        Call<GetAllVoucherRes> getAllVoucher = pool.getApiCallUserVoucher().getAllVoucher(uid);
        getAllVoucher.enqueue(new Callback<GetAllVoucherRes>() {
            @Override
            public void onResponse(Call<GetAllVoucherRes> call, Response<GetAllVoucherRes> response) {
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
                    listUserVoucher.setValue(response.body().getVouchers());
                }
            }

            @Override
            public void onFailure(Call<GetAllVoucherRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadAllUserPaymentMethod(int uid) {
        pool = new Pool();
        Call<GetAllMethod> getAllVoucher = pool.getApiCallUserMethod().getAllMethod(uid);
        getAllVoucher.enqueue(new Callback<GetAllMethod>() {
            @Override
            public void onResponse(Call<GetAllMethod> call, Response<GetAllMethod> response) {
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
                    ArrayList<MethodWithTypeName> res =response.body().getList();
                    HashMap<String, ArrayList<MethodWithTypeName>> mapMethod= new HashMap<>();
                    for(MethodWithTypeName m: res) {
                        ArrayList<MethodWithTypeName> addedEl = mapMethod.containsKey(m.getType())
                                ? mapMethod.get(m.getType())
                                : new ArrayList<>();

                        addedEl.add(m);
                        mapMethod.put(m.getType(), addedEl);
                    }
                    listUserPaymentMethod.setValue(mapMethod);
                }
            }

            @Override
            public void onFailure(Call<GetAllMethod> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadAllMethodSupport() {
        pool = new Pool();
        Call<MethodSupportRes> getAllMethodSupport = pool.getApiCallGeneral().getSystemMethod();
        getAllMethodSupport.enqueue(new Callback<MethodSupportRes>() {
            @Override
            public void onResponse(Call<MethodSupportRes> call, Response<MethodSupportRes> response) {
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
                    listAllMethodSupport.setValue(response.body().getMethod());
                }
            }

            @Override
            public void onFailure(Call<MethodSupportRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadOrderState(int uid, String type) {
        pool = new Pool();

        Call<TrackOrderInStateRes> orderActive = pool.getApiCallUserOrder().listOrderInState(uid, type);
        orderActive.enqueue(new Callback<TrackOrderInStateRes>() {
            @Override
            public void onResponse(Call<TrackOrderInStateRes> call, Response<TrackOrderInStateRes> response) {
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
                    switch (type) {
                        case "active":
                            listOrderActive.setValue(response.body().getOrders());
                            break;
                        case "completed":
                            listOrderCompleted.setValue(response.body().getOrders());
                            break;
                        case "cancelled":
                            listOrderCancelled.setValue(response.body().getOrders());
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<TrackOrderInStateRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadSystemOrderState() {
        pool = new Pool();

        Call<OrderStateRes> getOrderState = pool.getApiCallGeneral().getSystemState();
        getOrderState.enqueue(new Callback<OrderStateRes>() {
            @Override
            public void onResponse(Call<OrderStateRes> call, Response<OrderStateRes> response) {
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
                    listOrderState.setValue(response.body().getState());
                }
            }

            @Override
            public void onFailure(Call<OrderStateRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadReasonSystem() {
        pool = new Pool();

        Call<CancelReasonRes> getReasonSystem = pool.getApiCallGeneral().getSystemCancelReason();
        getReasonSystem.enqueue(new Callback<CancelReasonRes>() {
            @Override
            public void onResponse(Call<CancelReasonRes> call, Response<CancelReasonRes> response) {
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
                    listReason.setValue(response.body().getReasons());
                }
            }

            @Override
            public void onFailure(Call<CancelReasonRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

}
