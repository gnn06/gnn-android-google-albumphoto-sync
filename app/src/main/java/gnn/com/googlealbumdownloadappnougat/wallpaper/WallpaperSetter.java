package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;
import gnn.com.photos.sync.WallpaperObserver;

/**
 * Set a wallpaper. Called by Chooser.
 * params :
 * - folder where choose a
 * - android context to create log
 * Used for schedule or one time wallpaper
 */
public class WallpaperSetter implements WallpaperObserver {
    private static final String TAG = "PhotoWallPaper";
    private final Context activity;

    public WallpaperSetter(Context activity) {
        this.activity = activity;
    }

    @Override
    public void onWallpaper(Photo photo) {
        Bitmap bitmap = getBitmap(photo.getPhotoLocalFile(getPhotoFolder()).getAbsolutePath());
        setWallpaper(bitmap);
    }

    private File getPhotoFolder() {
        String album = new PersistPrefMain(activity).getAlbum();
        File directory = Environment.getExternalStoragePublicDirectory(album);
        return directory;
    }

    private Bitmap getBitmap(@Nonnull String path) {
        return BitmapFactory.decodeFile(path);
    }

    private void setWallpaper(Bitmap bitmap) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
        Logger logger = Logger.getLogger();
        try {
            Point point = getScreenSize();
            // portail = 1080x1977 and paysage = 1977x1080
            logger.finest("screen size " + point.x + "x" + point.y);
            Log.i(TAG, "screen size " + point.x + "x" + point.y);
            Bitmap scaledBitmap = PhotoScaleAndroid.scale(bitmap, point.x, point.y);
            wallpaperManager.setBitmap(scaledBitmap);
            logger.info("wallpaper correctly set");
        } catch (IOException e) {
            logger.severe("WallpaperManager error " + e.getMessage());
            Log.e("WALLPAPER", e.getMessage());
        }
    }

    @NonNull
    Point getScreenSize() {
        // As not un activity, get windowsmanager throught SystemService and not Activity.getWiindowManager
        WindowManager wm = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point;
    }
}