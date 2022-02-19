package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.io.File;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;
import gnn.com.photos.sync.PersistChoose;

/**
 * Set a wallpaper. Called by Chooser.
 * params :
 * - folder where choose a
 * - android context to create log
 * Used for schedule or one time wallpaper
 */
public class WallpaperSetter {
    private static final String TAG = "PhotoWallPaper";
    private final Context activity;

    public WallpaperSetter(Context activity) {
        this.activity = activity;
    }

    public void refreshFromCurrent(SurfaceHolder holder) {
        File processFolder = activity.getApplicationContext().getFilesDir();
        Photo currentPhoto = new PersistChoose(processFolder).getCurrentPhoto();
        if (currentPhoto != null) {
            Bitmap bitmap = getBitmap(currentPhoto.getPhotoLocalFile(getPhotoFolder()).getAbsolutePath());
            setWallpaper(bitmap, holder);
        } else {
            noPhotoWallpaper(holder);
        }
    }

    void setWallpaper(Photo photo, SurfaceHolder holder) {
        if (photo != null) {
            Bitmap bitmap = getBitmap(photo.getPhotoLocalFile(getPhotoFolder()).getAbsolutePath());
            setWallpaper(bitmap, holder);
        }
    }

    private File getPhotoFolder() {
        String album = new PersistPrefMain(activity).getPhotoPath();
        File directory = Environment.getExternalStoragePublicDirectory(album);
        return directory;
    }

    private Bitmap getBitmap(@Nonnull String path) {
        return BitmapFactory.decodeFile(path);
    }

    private void setWallpaper(Bitmap bitmap, SurfaceHolder holder) {
        // WallpaperManager.setBitmap set bitmap but unset current live wallpaper
        Logger logger = Logger.getLogger();
        Point point = getScreenSize();
        // WindowManager.getSize = portail = 1080x1977 and paysage = 1977x1080
        // surface = 1080x2280
        logger.finest("screen size " + point.x + "x" + point.y);
        Log.i(TAG, "screen size " + point.x + "x" + point.y);
        if (holder != null) {
            Canvas canvas = holder.lockCanvas();
            Paint paint = new Paint();
            if (canvas != null) {
                Matrix matrix = PhotoScaleAndroid.getMatrix(bitmap, canvas.getWidth(), canvas.getHeight());
                canvas.drawBitmap(bitmap, matrix, paint);
            }
            holder.unlockCanvasAndPost(canvas);
            logger.info("wallpaper correctly set");
            Log.i(TAG, "wallpaper correctly set");
        }
    }

    private void noPhotoWallpaper(SurfaceHolder holder) {
        if (holder != null) {
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                Paint paint = new Paint();
                Point point = getScreenSize();
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                double relation = Math.sqrt(canvas.getWidth() * canvas.getHeight()) / 20;
                paint.setTextSize((float) (relation));
                canvas.drawText("Pas de photo", point.x/2, point.y/2, paint);
            }
            holder.unlockCanvasAndPost(canvas);
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