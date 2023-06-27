package com.wallpaperai.wallpaperhelper;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.content.Intent;
import android.app.Activity;
import com.yalantis.ucrop.UCrop;

@CapacitorPlugin(name = "WallpaperHelper")
public class WallpaperHelperPlugin extends Plugin {

    private WallpaperHelper implementation = new WallpaperHelper();
    private PluginCall savedCall;

    @PluginMethod
    public void setWallpaper(PluginCall call) {
        String base64Image = call.getString("base64");
        // boolean isLockScreen = call.getBoolean("isLockScreen", false); // default to false if not provided

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
