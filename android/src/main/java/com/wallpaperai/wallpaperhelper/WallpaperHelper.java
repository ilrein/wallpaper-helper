package com.wallpaperai.wallpaperhelper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WallpaperHelper {

    public void setWallpaper(Activity activity, String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Convert bitmap to file and get Uri
        File file = new File(activity.getCacheDir(), "temp.jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            Log.e("WallpaperHelper", "Error writing file: " + e.getMessage());
            return;
        }
        Uri uri = Uri.fromFile(file);

        // Start the wallpaper setting activity
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
        Intent intent;
        intent = wallpaperManager.getCropAndSetWallpaperIntent(uri);
        activity.startActivityForResult(intent, REQUEST_SET_WALLPAPER);
    }


    // Call this method in your activity's onActivityResult method
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SET_WALLPAPER) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("WallpaperHelper", "Wallpaper set successfully");
            } else {
                Log.i("WallpaperHelper", "Wallpaper setting cancelled");
            }
        }
    }

    public static final int REQUEST_SET_WALLPAPER = 1;

}
