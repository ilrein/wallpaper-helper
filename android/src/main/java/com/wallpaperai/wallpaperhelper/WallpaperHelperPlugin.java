package com.wallpaperai.wallpaperhelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.theartofdev.edmodo.cropper.CropImage;

@CapacitorPlugin(name = "WallpaperHelper")
public class WallpaperHelperPlugin extends Plugin {

    private WallpaperHelper implementation = new WallpaperHelper();

    public WallpaperHelperPlugin() {
        super();
        Log.v("WallpaperHelperPlugin", "Plugin loaded");
    }

    @PluginMethod
    public void setWallpaper(PluginCall call) {
        String base64Image = call.getString("base64");
        boolean setBoth = call.getBoolean("setBoth", false);

        try {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            implementation.startCroppingActivity(getContext(), bitmap);

            // Save the call for later
            saveCall(call);
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @ActivityCallback
    private void handleActivityResult(PluginCall call, ActivityResult result) {
        if (call == null) {
            return;
        }

        boolean setBoth = call.getBoolean("setBoth", false);
        implementation.handleActivityResult(getActivity(), result.getResultCode(), result.getData(), setBoth);
        JSObject ret = new JSObject();
        ret.put("value", "Success");
        call.resolve(ret);
    }
}
