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
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WallpaperHelper {

    public void startCroppingActivity(Context context, Bitmap bitmap) {
        // Convert bitmap to file and get Uri
        File cachePath = new File(context.getCacheDir(), "images");
        cachePath.mkdirs();
        File filePath = new File(cachePath, "image.png");
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            Log.e("WallpaperHelper", "Error writing file: " + e.getMessage());
            return;
        }
        String authority = context.getApplicationContext().getPackageName() + ".fileprovider";
        Uri sourceUri = FileProvider.getUriForFile(context, authority, filePath);

        // Start the cropping activity with uCrop
        UCrop.of(sourceUri, sourceUri).start((Activity) context);
    }

    public void handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            Uri resultUri = UCrop.getOutput(data);

            // Set the cropped image as wallpaper
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
            try (InputStream in = activity.getContentResolver().openInputStream(resultUri)) {
                if (in != null) {
                    wallpaperManager.setStream(in);
                    Log.v("WallpaperHelper", "Wallpaper set successfully");
                }
            } catch (IOException e) {
                Log.e("WallpaperHelper", "Error setting wallpaper: " + e.getMessage());
            }
        }
    }
}
