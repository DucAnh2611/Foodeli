package com.example.foodeli.Fragments.ShopProduct;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodeli.Fragments.Profile.MenuSelectImage;
import com.example.foodeli.Fragments.Profile.OnSelectMenuPicture;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.MySqlSetUp.Schemas.Product.Product;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body.UpdateProductBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.CreateProductRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apmem.tools.layouts.FlowLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogProductForm extends DialogFragment {

    private static final int REQUEST_CODE_LIBRARY_PICTURE = 1;
    private static final int CAMERA_REQUEST_CODE = 100, CAMERA_PERMISSION_REQUEST_CODE =50;
    private Context context;
    private OnMethodProductForm onMethodProductForm;
    private Pool pool;
    private RecyclerView imageRV;
    private AppCompatButton addBtn, newCateBtn, cancel, confirm;
    private EditText pName, pPrice, pUnit, pQuantity, pShortDesc, pLongDesc, pFrom, pTo;
    private FlowLayout listCategory;
    private int pid = 0;
    private ListProductImageAdapter adapter;
    private ArrayList<Category> categories;
    private ArrayList<UpdateProductBody.ImageState> newImageList;
    private ArrayList<UpdateProductBody.CategoryState> newCategoryIdList;
    private String name, shortDesc, longDesc, unit;
    private int quantity, from, to;
    private float price;
    private DialogSelectCategory dialogSelectCategory;
    private MenuSelectImage dialog;
    private Product info;
    private ArrayList<CreateProductRes.Category> productCate;
    private ArrayList<CreateProductRes.Image> productImage;
    private String newProductImage= "";
    private SupportImage supportImage = new SupportImage();

    public  DialogProductForm(Context context, ArrayList<Category> list) {
        this.context = context;
        this.categories = list;
    }

    public  DialogProductForm(Context context, int pid, ArrayList<Category> list) {
        this.context = context;
        this.pid = pid;
        this.categories = list;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_form_product, null);
        dialog.setContentView(view);

        newImageList = new ArrayList<>();
        newCategoryIdList = new ArrayList<>();

        if(pid != 0) {
            getProductInformation(pid);
        }
        else {
            name = shortDesc = longDesc = unit = "";
            quantity = from = to = 0;
            price = 0;
            productCate = new ArrayList<>();
            productImage = new ArrayList<>();
        }

        imageRV = view.findViewById(R.id.dialog_shop_form_product_image_rv);
        addBtn = view.findViewById(R.id.dialog_shop_form_product_add_image);
        newCateBtn = view.findViewById(R.id.dialog_shop_form_product_add_cate);
        pName = view.findViewById(R.id.dialog_shop_form_product_name);
        pPrice = view.findViewById(R.id.dialog_shop_form_product_price);
        pUnit = view.findViewById(R.id.dialog_shop_form_product_unit);
        pQuantity = view.findViewById(R.id.dialog_shop_form_product_quantity);
        pShortDesc = view.findViewById(R.id.dialog_shop_form_product_short);
        pLongDesc = view.findViewById(R.id.dialog_shop_form_product_long);
        pFrom = view.findViewById(R.id.dialog_shop_form_product_from);
        pTo = view.findViewById(R.id.dialog_shop_form_product_to);
        listCategory = view.findViewById(R.id.dialog_shop_form_product_flow_layout);
        cancel = view.findViewById(R.id.dialog_shop_form_product_cancel);
        confirm = view.findViewById(R.id.dialog_shop_form_product_confirm);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicturePicker();
            }
        });
        newCateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSelectCategory();
            }
        });

        pName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) name = s.toString();
            }
        });
        pPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    price = Float.parseFloat(s.toString());
                    if(price < 0) {
                        price = 0;
                        pPrice.setText(String.valueOf(price));
                    }
                };
            }
        });
        pUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) unit = s.toString();
            }
        });
        pQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    quantity = Integer.parseInt(s.toString());
                    if(quantity < 0) {
                        quantity = 0;
                        pQuantity.setText(String.valueOf(quantity));
                    }
                }
            }
        });
        pShortDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) shortDesc = s.toString();
            }
        });
        pLongDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) longDesc = s.toString();
            }
        });
        pFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    from = Integer.parseInt(s.toString());
                }

            }
        });
        pTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    to = Integer.parseInt(s.toString());
                }

            }
        });

        imageRV.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ListProductImageAdapter(context, new ArrayList<>());
        imageRV.setAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMethodProductForm.onCancel();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProductBody body = new UpdateProductBody(0, 0, price, quantity, from, to, name, unit, shortDesc, longDesc, newImageList, newCategoryIdList);
                if(from >= to || from <= 0) {
                    Toast.makeText(context, "Invalid Estimate time", Toast.LENGTH_SHORT).show();
                }
                else {
                    for (int i = 0; i < newImageList.size(); i++) {
                        if(newImageList.get(i).getState() != -1 && !newImageList.get(i).getBase64().equals("")) {
                            int tId= newImageList.get(i).getIid();

                            if(tId == 0) {
                                for (CreateProductRes.Image img: productImage) {
                                    if(img.getIid() == tId) {
                                        newProductImage = img.getBase64();
                                        break;
                                    }
                                }
                            }
                            else{
                                newProductImage = newImageList.get(i).getBase64();
                            }
                            onMethodProductForm.onConfirm(body, pid, newProductImage);
                            break;
                        }
                    }
                }
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    public interface OnMethodProductForm {
        void onCancel();
        void onConfirm(UpdateProductBody body, int pid, String newImage);
    }
    private void ShowSelectCategory() {
        dialogSelectCategory = new DialogSelectCategory( categories, context );
        dialogSelectCategory.show(getActivity().getSupportFragmentManager(), "dialog_category_list");
        dialogSelectCategory.setOnApplyCategory(new DialogSelectCategory.OnApplyCategory() {
            @Override
            public void onApply(int cid) {
                boolean isExist = false;
                
                UpdateProductBody.CategoryState cateFit = null;

                for (UpdateProductBody.CategoryState cate : newCategoryIdList) {
                    if (cate.getCid() == cid) {
                        cateFit = cate;
                        isExist = true;
                        break;
                    }
                }
                if( !isExist ){
                    newCategoryIdList.add(new UpdateProductBody.CategoryState(cid, 1));
                    for (int i = 0; i < categories.size(); i++) {
                        if(categories.get(i).getCid() == cid) {
                            Category cate = categories.get(i);
                            productCate.add(new CreateProductRes.Category(cate.getCid(), cate.getName(), cate.getImage(), cate.getP_cid()));
                            break;
                        }
                    }
                }
                else {
                    newCategoryIdList.set(newCategoryIdList.indexOf(cateFit), new UpdateProductBody.CategoryState(cateFit.getCid(), 1));
                    Toast.makeText(context, "This Category added already", Toast.LENGTH_SHORT).show();
                }
                updateUiCategory(productCate);
            }
        });
    }
    private void getProductInformation(int pid) {
        pool = new Pool();

        Call<CreateProductRes> getProduct = pool.getApiCallShopProduct().getProductData(pid);

        getProduct.enqueue(new Callback<CreateProductRes>() {
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
                    CreateProductRes.P res = response.body().getProduct();
                    adapter = new ListProductImageAdapter(getContext(), res.getImages());
                    imageRV.setAdapter(adapter);

                    productCate = res.getCategories();
                    productImage = res.getImages();
                    info = res.getProduct();
                    name = info.getName();
                    price = info.getPrice();
                    unit = info.getUnit();
                    quantity = info.getStock();
                    shortDesc = info.getShortDesc();
                    longDesc = info.getLongDesc();
                    from = Integer.parseInt(info.getTimeStart());
                    to = Integer.parseInt(info.getTimeFinish());

                    pName.setText(name);
                    pPrice.setText(String.valueOf(price));
                    pUnit.setText(unit);
                    pQuantity.setText(String.valueOf(quantity));
                    pShortDesc.setText(shortDesc);
                    pLongDesc.setText(longDesc);
                    pFrom.setText(String.valueOf(from));
                    pTo.setText(String.valueOf(to));

                    for (CreateProductRes.Category cate: productCate) {
                        newCategoryIdList.add(new UpdateProductBody.CategoryState(cate.getId(), 0));
                    }

                    for (CreateProductRes.Image image: productImage) {
                        newImageList.add(new UpdateProductBody.ImageState(image.getIid(), "", 0));
                    }

                    updateUiCategory(productCate);

                    adapter.setOnDeleteImage(new ListProductImageAdapter.OnDeleteImage() {
                        @Override
                        public void onDelete(int iid, int position) {
                            UpdateProductBody.ImageState image = newImageList.get(position);
                            productImage.remove(position);
                            newImageList.remove(position);

                            if(image.getIid() != 0) {
                                newImageList.add(position, new UpdateProductBody.ImageState(image.getIid(), "", -1));
                            }
                            adapter.setList(productImage);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<CreateProductRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }
    private void updateUiCategory(ArrayList<CreateProductRes.Category> categories) {
        if(listCategory.getChildCount()-1 > 0) listCategory.removeViews(1, listCategory.getChildCount() - 1);

        for (int i = 0; i < categories.size(); i++) {
            if(newCategoryIdList.get(i).getState() != -1) {
                CreateProductRes.Category category = categories.get(i);

                TextView textView = new TextView(getContext());
                textView.setText(category.getName());
                textView.setBackground(getResources().getDrawable(R.drawable.custom_input_style_stroke_green100_max_radius));

                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT,
                        FlowLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 20, 10);
                textView.setLayoutParams(params);
                textView.setPadding(30, 7, 30, 7);
                textView.setTextSize(12);

                int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newCategoryIdList.set(finalI, new UpdateProductBody.CategoryState(category.getId(), -1));
                        listCategory.removeView(textView);
                    }
                });

                listCategory.addView(textView);
            }
        }
    }
    private void showPicturePicker() {
        dialog = new MenuSelectImage(getContext());
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_select_avatar");
        dialog.setOnSelectMenuPicture(new OnSelectMenuPicture() {
            @Override
            public void onTakePicture() {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // The permission is not granted, request it
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    startImageCaptureIntent();
                }
            }

            @Override
            public void onSelectFromLibrary() {

                openLibraryPictureChooser();
            }
        });
    }
    private void openLibraryPictureChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_LIBRARY_PICTURE);
    }
    private void startImageCaptureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getContext(), "No camera application is available", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageCaptureIntent();
            } else {
                Toast.makeText(getContext(), "Camera permission is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = null;

        if (requestCode == REQUEST_CODE_LIBRARY_PICTURE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imageBitmap = getBitmapFromUri(selectedImageUri);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = supportImage.convert(supportImage.CompressBitMap(150, (Bitmap) extras.get("data")));
        }
        if(imageBitmap != null) {
            String imageBase64 = supportImage.bitmapToBase64(imageBitmap);
            newImageList.add(new UpdateProductBody.ImageState(0, imageBase64, 1));
            productImage.add(new CreateProductRes.Image(0, imageBase64));

            adapter.setList(productImage);
            adapter.setOnDeleteImage(new ListProductImageAdapter.OnDeleteImage() {
                @Override
                public void onDelete(int iid, int position) {
                    UpdateProductBody.ImageState image = newImageList.get(position);
                    productImage.remove(position);
                    newImageList.remove(position);

                    if(image.getIid() != 0) {
                        newImageList.add(position, new UpdateProductBody.ImageState(image.getIid(), "", -1));
                    }
                    adapter.setList(productImage);
                }
            });
            dialog.dismiss();
        }

    }
    public Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            byte[] compressedImage = supportImage.CompressBitMap(100, bitmap);
            bitmap = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);

            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnMethodProductForm(OnMethodProductForm onMethodProductForm) {
        this.onMethodProductForm = onMethodProductForm;
    }
}
