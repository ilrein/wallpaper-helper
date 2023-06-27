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

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WallpaperHelper {

    public void setWallpaper(Activity activity, String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Convert bitmap to file and get Uri
        File cachePath = new File(activity.getCacheDir(), "images");
        cachePath.mkdirs();
        File filePath = new File(cachePath, "image.jpg");
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            Log.e("WallpaperHelper", "Error writing file: " + e.getMessage());
            return;
        }
        String authority = activity.getApplicationContext().getPackageName() + ".fileprovider";
        Uri uri = FileProvider.getUriForFile(activity, authority, filePath);

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
