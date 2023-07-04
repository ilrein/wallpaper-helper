package com.wallpaperai.wallpaperhelper;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.content.Intent;
import android.app.Activity;
import com.yalantis.ucrop.UCrop;
import android.util.Log;

@CapacitorPlugin(name = "WallpaperHelper")
public class WallpaperHelperPlugin extends Plugin {

    private WallpaperHelper implementation = new WallpaperHelper();
    private PluginCall savedCall;

    public WallpaperHelperPlugin() {
        super();
        Log.v("WallpaperHelperPlugin", "Plugin loaded");
    }

    @PluginMethod
    public void setWallpaper(PluginCall call) {
        String base64Image = call.getString("base64");

        // Save the call so we can resolve it in handleOnActivityResult
        savedCall = call;
        
        try {
            implementation.setWallpaper(getActivity(), base64Image);
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        Log.v("handleOnActivityResult Plugin", "requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == UCrop.REQUEST_CROP) {
            Log.v("handleOnActivityResult Plugin", "Request code matches UCrop.REQUEST_CROP");
        } else {
            Log.v("handleOnActivityResult Plugin", "Request code does not match UCrop.REQUEST_CROP");
        }
        
        if (savedCall == null) {
            return;
        }
        
        JSObject ret = new JSObject();

        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                ret.put("value", "Success");
                savedCall.resolve(ret);
            } else {
                savedCall.reject("Cancelled");
            }
            savedCall = null;
        }
    }
}
