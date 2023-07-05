package com.wallpaperai.wallpaperhelper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WallpaperHelper {

    public String setWallpaper(Context context, String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        int inSampleSize = 1;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int reqHeight = displayMetrics.heightPixels;
        int reqWidth = displayMetrics.widthPixels;
        if (imageHeight > reqHeight || imageWidth > reqWidth) {
            final int halfHeight = imageHeight / 2;
            final int halfWidth = imageWidth / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, bitmapOptions);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(bitmap);
            return "Success";
        } catch (IOException e) {
            return "Error setting wallpaper: " + e.getMessage();
        }
    }
}
