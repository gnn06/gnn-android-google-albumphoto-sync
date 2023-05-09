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
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.File;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ScreenSizeAccessor;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;
import gnn.com.photos.sync.PersistChoose;
import gnn.com.util.ScreenSize;

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
        File processFolder = ApplicationContext.getInstance(activity).getProcessFolder();
        Photo currentPhoto = new PersistChoose(processFolder).getCurrentPhoto();
        Bitmap bitmap = null;
        if (currentPhoto != null) {
            bitmap = getBitmap(currentPhoto.getPhotoLocalFile(getPhotoFolder()).getAbsolutePath());
        }
        if (bitmap != null) {
            setWallpaper(bitmap, holder);
        } else {
            noPhotoWallpaper(holder);
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
        ScreenSize point = getScreenSize();
        // WindowManager.getSize = portail = 1080x1977 and paysage = 1977x1080
        // surface = 1080x2280
        logger.finest("screen size " + point.width + "x" + point.height);
        Log.i(TAG, "screen size " + point.width + "x" + point.height);
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
                ScreenSize point = getScreenSize();
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                double relation = Math.sqrt(canvas.getWidth() * canvas.getHeight()) / 20;
                paint.setTextSize((float) (relation));
                canvas.drawText("Pas de photo", point.width/2, point.height/2, paint);

                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.livewallpaper_preview, null);
                System.out.println("start"+view.getMeasuredWidth()+","+view.getWidth());
                view.measure(view.getMeasuredWidth(), view.getMeasuredHeight());
                System.out.println("measure"+view.getMeasuredWidth()+","+view.getWidth());
                view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());
                System.out.println("layout"+view.getMeasuredWidth()+","+view.getWidth());
                canvas.save();
                canvas.translate((point.width - view.getWidth())/2, (point.height - view.getHeight())/2);
                view.draw(canvas);
                canvas.restore();
                // start   = 0,0
                // measure = 834, 0
                // layout  = 834, 834
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    @NonNull
    ScreenSize getScreenSize() {
        return new ScreenSizeAccessor(activity).get();
    }
}