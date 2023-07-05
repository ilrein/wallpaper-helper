package com.wallpaperai.wallpaperhelper;

import android.util.Log;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

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
            String result = implementation.setWallpaper(getContext(), base64Image);
            JSObject ret = new JSObject();
            ret.put("value", result);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }
}
