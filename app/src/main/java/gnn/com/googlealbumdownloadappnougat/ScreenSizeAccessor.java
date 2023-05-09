package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import gnn.com.photos.service.IScreenSizeAccessor;
import gnn.com.util.ScreenSize;

public class ScreenSizeAccessor implements IScreenSizeAccessor {

    private final Context context;

    public ScreenSizeAccessor(Context context) {
        this.context = context;
    }

    public ScreenSize get() {
        // As not un activity, get windowsmanager throught SystemService and not Activity.getWiindowManager
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return new ScreenSize(point.x, point.y);
    }
}
