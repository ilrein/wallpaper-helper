package com.wallpaperai.wallpaperhelper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.IOException;

public class WallpaperHelper {

    public String setWallpaper(Context context, String base64Image, boolean setBoth) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);

        try {
            if (setBoth) {
                wallpaperManager.setBitmap(bitmap);
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            } else {
                wallpaperManager.setBitmap(bitmap);
            }
            return "Success";
        } catch (IOException e) {
            return "Error setting wallpaper: " + e.getMessage();
        }
    }
}
