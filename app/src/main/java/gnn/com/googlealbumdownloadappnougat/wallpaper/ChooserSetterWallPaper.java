package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.googlealbumdownloadappnougat.wallpaper.stat.PersistWallpaperStat;
import gnn.com.googlealbumdownloadappnougat.wallpaper.stat.StatUpdaterWithPersist;
import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.sync.PersistWallpaperTime;
import gnn.com.photos.sync.PhotoChooser;

/**
 * Choose a photo and set the wall paper
 * params :
 * - folder where choose a
 * - android context to create log
 */
public class ChooserSetterWallPaper {
    private static final String TAG = "PhotoWallPaper";
    private final Context activity;
    private final File photoFolder;
    private File processFolder;

    public ChooserSetterWallPaper(Context activity, File photoFolder, File processFolder) {
        this.activity = activity;
        this.photoFolder = photoFolder;
        this.processFolder = processFolder;
    }

    public void setWallpaper() {
        Logger logger = Logger.getLogger();
        Photo photo = chooseLocalPhoto(photoFolder);
        logger.info("wallpaper.setWallpaper has choose " + photo.getId());
        if (photo != null) {
            Bitmap bitmap = getBitmap(photo.getPhotoLocalFile(photoFolder).getAbsolutePath());
            setWallpaper(bitmap);
            try {
                new PersistWallpaperTime(this.processFolder).storeTime();
                new StatUpdaterWithPersist(new PersistWallpaperStat(this.processFolder), new DateProvider()).onWallpaperChange();
            } catch (IOException e) {
                Log.e(TAG, "can not write last wallpaper time");
            }
        } else {
            Log.w(TAG, "no photo to use as wallpaper");
        }
    }

    /**
     *
     * @return null if no photo found
     * @param folder
     */
    private Photo chooseLocalPhoto(File folder) {
        PhotosLocalService pls = new PhotosLocalService();
        ArrayList<Photo> localPhotos = pls.getLocalPhotos(folder);
        if (localPhotos.size() > 0) {
            Logger logger = Logger.getLogger();
            ArrayList<Photo> photos = new PhotoChooser().chooseOneList(localPhotos, 1, null);
            return photos.get(0);
        }
        // TODO manage no local photo
        return null;
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