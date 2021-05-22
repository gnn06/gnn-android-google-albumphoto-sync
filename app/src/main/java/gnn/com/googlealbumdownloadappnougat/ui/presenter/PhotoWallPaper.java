package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    void setWallpaper() {
        Bitmap bitmap = getBitmap();
        setWallpaper(bitmap);
    }

    private Bitmap getBitmap() {
        // Pixel
        @SuppressLint("SdCardPath")
        String path = "/sdcard/Pictures/ADoMfeQj6d-sExfOjnrmN0QHHGRERVUz4Id9o4QmChvwZSqHTZgEnn4QbZkKkaqbq8ym-5zOaY4nOsUQefGenJAGHe9y5CTBUQ.jpg";
        // Oneplus
//        final String path = "/sdcard/Pictures/Wallpaper/ADoMfeQopV_9xE6Wi9Uz1CWFVNiDjtPjbCv5dexK9a-_F-F_n8hBcuD2Hf2Ez8CTQVIf7ev54r8mBmvXwo2oU--vu7KhR-L6uw.jpg";
        return BitmapFactory.decodeFile(path);
    }

    private void setWallpaper(Bitmap bitmap) {
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