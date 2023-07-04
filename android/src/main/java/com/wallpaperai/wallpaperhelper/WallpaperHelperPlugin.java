package com.wallpaperai.wallpaperhelper;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.content.Intent;
import android.app.Activity;
import androidx.core.content.FileProvider; // Add this import statement

@CapacitorPlugin(name = "WallpaperHelper")
public class WallpaperHelperPlugin extends Plugin {

    private WallpaperHelper implementation = new WallpaperHelper();
    private PluginCall savedCall;

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

        if (savedCall == null) {
            return;
        }

        JSObject ret = new JSObject();

        if (requestCode == WallpaperHelper.REQUEST_SET_WALLPAPER) {
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
