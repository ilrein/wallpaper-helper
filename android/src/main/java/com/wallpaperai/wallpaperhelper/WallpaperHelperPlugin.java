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
import com.yalantis.ucrop.UCrop;

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

            saveCall(call);
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            Log.e("WallpaperHelperPlugin", "No stored plugin call for onActivityResult");
            return;
        }

        if (requestCode == UCrop.REQUEST_CROP && resultCode == getActivity().RESULT_OK) {
            implementation.handleActivityResult(getActivity(), requestCode, resultCode, data);
            JSObject ret = new JSObject();
            ret.put("value", "Success");
            savedCall.resolve(ret);
        } else {
            savedCall.reject("Failed to set wallpaper");
        }
    }
}
