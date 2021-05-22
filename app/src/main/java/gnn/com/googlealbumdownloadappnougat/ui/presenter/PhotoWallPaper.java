package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.photos.PhotoScaleAndroid;

public class PhotoWallPaper {
    private final MainActivity activity;

    public PhotoWallPaper(MainActivity activity) {
        this.activity = activity;
    }

    void setWallpaper(Bitmap bitmap) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
        try {
            Point point = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(point);
            Bitmap scaledBitmap = PhotoScaleAndroid.scale(bitmap, point.x, point.y);
            wallpaperManager.setBitmap(scaledBitmap);
        } catch (IOException e) {
            Log.e("WALLPAPER", e.getMessage());
        }
    }
}