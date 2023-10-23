package com.example.foodeli.Supports;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class SupportImage {
    public Bitmap convertBase64ToBitmap(String image) {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public String bitmapToBase64(Bitmap bitmap) {

        byte[] byteArray = CompressBitMap(100, bitmap);

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public byte[] CompressBitMap(int maxKb, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        int qualityThreshold = maxKb * 1024; // Define the desired size threshold in bytes

        // Compress the image until the size is lower than the quality threshold
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        while (byteArrayOutputStream.toByteArray().length > qualityThreshold && quality > 5) {
            byteArrayOutputStream.reset();
            quality -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream); // Adjust the quality percentage as needed
        }

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }

    public Bitmap toGrayscale(Bitmap originalBitmap) {
        int width, height;
        height = originalBitmap.getHeight();
        width = originalBitmap.getWidth();

        Bitmap grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayscaleBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(originalBitmap, 0, 0, paint);

        return grayscaleBitmap;
    }

    public Bitmap convert(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }
}
