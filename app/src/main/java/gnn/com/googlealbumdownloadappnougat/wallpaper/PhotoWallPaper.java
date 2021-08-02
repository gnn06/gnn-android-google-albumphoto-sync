package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.sync.PhotoChooser;

public class PhotoWallPaper {
    private static final String TAG = "PhotoWallPaper";
    private final Activity activity;
    private final File folder;

    public PhotoWallPaper(Activity activity, File folder) {
        this.activity = activity;
        this.folder = folder;
    }

    public void setWallpaper() {
        Photo photo = chooseLocalPhoto(folder);
        if (photo != null) {
            Bitmap bitmap = getBitmap(photo.getPhotoLocalFile(folder).getAbsolutePath());
            setWallpaper(bitmap);
        } else {
            Log.w(TAG, "photo to use as wallpaper");
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