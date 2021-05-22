package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.photos.PhotoScaleAndroid;
import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.sync.PhotoChooser;

public class PhotoWallPaper {
    private final MainActivity activity;
    private final File folder;

    public PhotoWallPaper(MainActivity activity, File folder) {
        this.activity = activity;
        this.folder = folder;
    }

    void setWallpaper() {
        Photo photo = chooseLocalPhoto(folder);
        Bitmap bitmap = getBitmap(photo.getPhotoLocalFile(folder).getAbsolutePath());
        if (bitmap != null) {
            setWallpaper(bitmap);
        }
    }

    private Bitmap getBitmap(String path) {
        return path != null ? BitmapFactory.decodeFile(path) : null;
    }

    /**
     *
     * @return null if no photo found
     * @param folder
     */
    private Photo chooseLocalPhoto(File folder) {
        // Pixel
        @SuppressLint("SdCardPath")
        String path = "/sdcard/Pictures/ADoMfeQj6d-sExfOjnrmN0QHHGRERVUz4Id9o4QmChvwZSqHTZgEnn4QbZkKkaqbq8ym-5zOaY4nOsUQefGenJAGHe9y5CTBUQ.jpg";
        // Oneplus
//        final String path = "/sdcard/Pictures/Wallpaper/ADoMfeQopV_9xE6Wi9Uz1CWFVNiDjtPjbCv5dexK9a-_F-F_n8hBcuD2Hf2Ez8CTQVIf7ev54r8mBmvXwo2oU--vu7KhR-L6uw.jpg";
        PhotosLocalService pls = new PhotosLocalService();
        ArrayList<Photo> localPhotos = pls.getLocalPhotos(folder);
        if (localPhotos.size() > 0) {
            ArrayList<Photo> photos = new PhotoChooser().chooseOneList(localPhotos, 1);
            return photos.get(0);
        }
        return null;
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