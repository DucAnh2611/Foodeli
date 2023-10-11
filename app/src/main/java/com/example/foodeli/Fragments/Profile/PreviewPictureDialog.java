package com.example.foodeli.Fragments.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.Activities.Cart.CartActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.ChangeForm;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.CreateUserResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewPictureDialog extends DialogFragment {

    private Context context;
    private Pool pool;
    private ShapeableImageView imagePreview;
    private AppCompatButton cancel, confirm;
    private ImageChangeInterface onConfirmImage;
    private SupportImage supportImage = new SupportImage();
    private int uid;

    Bitmap imageBitmap = null;

    public PreviewPictureDialog(Context context, Bitmap imageBitmap, int uid) {
        this.context = context;
        this.imageBitmap = imageBitmap;
        this.uid = uid;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_preview_image_change, null);
        dialog.setContentView(view);

        imagePreview = view.findViewById(R.id.preview_ava);
        cancel = view.findViewById(R.id.preview_ava_cancel);
        confirm = view.findViewById(R.id.preview_ava_confirm);

        imagePreview.setImageBitmap(imageBitmap);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserAvatar();
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    public void updateUserAvatar() {
        String base64 = supportImage.bitmapToBase64(imageBitmap);
        ArrayList<ChangeForm> body = new ArrayList<>();
        body.add(new ChangeForm("UserAvatar", base64));

        pool = new Pool();

        Call<CreateUserResponse> updateUserAva = pool.getApiCallUserProfile().update(uid, body);

        updateUserAva.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    Gson gson = new Gson();
                    SharedPreferences mPrefs = getActivity().getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
                    String json = mPrefs.getString("user", "");
                    User user = gson.fromJson(json, User.class);

                    user.setAvatar(base64);

                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    json = gson.toJson(user);
                    prefsEditor.putString("user", json);
                    prefsEditor.apply();

                    onConfirmImage.onConfirmImage();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    public void setOnConfirmImage(ImageChangeInterface onConfirmImage) {this.onConfirmImage = onConfirmImage;}

}
