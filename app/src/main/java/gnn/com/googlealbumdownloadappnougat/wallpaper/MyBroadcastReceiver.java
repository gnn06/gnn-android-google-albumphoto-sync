package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import gnn.com.googlealbumdownloadappnougat.util.Logger;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.configure(context.getCacheDir().getAbsolutePath());
        Logger logger = Logger.getLogger();
        PowerManager systemService = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        logger.fine("receive, idle=" + systemService.isDeviceIdleMode());
    }
}
