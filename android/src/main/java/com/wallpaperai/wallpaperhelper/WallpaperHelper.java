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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
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

        // Start the cropping activity with Android-Image-Cropper
        Log.v("WallpaperHelper", "startCroppingActivity");
        CropImage.activity(sourceUri).setGuidelines(CropImageView.Guidelines.ON).start((Activity) context);
    }

    public void handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data, boolean setBoth) {
        Log.v("WallpaperHelper", "handleActivityResult");
        Log.v("WallpaperHelper", String.valueOf(requestCode));
        Log.v("WallpaperHelper", String.valueOf(resultCode));
        Log.v("WallpaperHelper", String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE));

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();

                // Set the cropped image as wallpaper
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
                try (InputStream in = activity.getContentResolver().openInputStream(resultUri)) {
                    if (in != null) {
                        if (setBoth && android.os.Build.VERSION.SDK_INT >= 24) {
                            wallpaperManager.setStream(in, null, true, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                        } else {
                            wallpaperManager.setStream(in);
                        }
                        Log.v("WallpaperHelper", "Wallpaper set successfully");
                    }
                } catch (IOException e) {
                    Log.e("WallpaperHelper", "Error setting wallpaper: " + e.getMessage());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("WallpaperHelper", "Error during cropping: " + error.getMessage());
            }
        } else {
            Log.e("WallpaperHelper", "Unexpected requestCode: " + requestCode);
        }
    }
}
