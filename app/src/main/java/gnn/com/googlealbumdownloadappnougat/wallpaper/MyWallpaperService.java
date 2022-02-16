package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.graphics.Bitmap;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.photos.Photo;
import gnn.com.photos.sync.ChooseOneLocalPhotoPersist;
import gnn.com.photos.sync.PersistChoose;
import gnn.com.photos.sync.WallpaperObserver;

/**
 * onCreate, onSurfaceCreated, onSurfaceChanged, onSurfaceREdraNeeded, onVisibility true, false, true, onOffsetsChanged
 */

public class MyWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine implements WallpaperObserver {

        public MyWallpaperEngine() {}

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            /**
             * on reboot phone, it is called, previous wallpaper is not restored
             2022-02-12 15:08:23.999 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: observe added from livewallpaper create
             2022-02-12 15:08:24.013 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceCreated
             2022-02-12 15:08:24.013 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceChanged
             2022-02-12 15:08:24.013 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceRedrawNeeded
             2022-02-12 15:08:24.013 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             2022-02-12 15:08:24.013 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilityfalse
             2022-02-12 15:08:24.042 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onOffsetsChanged
             2022-02-12 15:08:24.042 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             2022-02-12 15:08:24.064 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onOffsetsChanged
             2022-02-12 15:08:31.801 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilityfalse
             2022-02-12 15:08:31.951 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onOffsetsChanged
             2022-02-12 15:08:31.951 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             2022-02-12 15:08:32.669 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilityfalse
             2022-02-12 15:08:32.679 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onOffsetsChanged
             2022-02-12 15:08:32.679 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             */
            super.onCreate(surfaceHolder);
            Log.d("GOI","onCreate, isPreview=" + isPreview());
            if (!isPreview()) {
                // TODO avoid have to give parameters
                File photoFolder = getFolder(new PersistPrefMain(getApplicationContext()).getPhotoPath());
                File processPath = getApplicationContext().getFilesDir();
                ChooseOneLocalPhotoPersist chooser = ChooseOneLocalPhotoPersist.getInstance(photoFolder, processPath);
                chooser.addObserver(this);
                Log.d("GOI-WALLPAPER","observe added from livewallpaper create");
                // TODO set wallpaper on create.
            }
        }

        @Override
        public void onWallpaper() {
            PersistChoose persistChoose = new PersistChoose(ApplicationContext.getInstance(getApplicationContext()).getProcessFolder());
            Photo photo = persistChoose.getCurrentPhoto();
            WallpaperSetter wallpaperSetter = new WallpaperSetter(getApplicationContext());
            wallpaperSetter.setWallpaper(photo, getSurfaceHolder());
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Log.d("GOI-WALLPAPER","onSurfaceCreated");
            WallpaperSetter wallpapersetter = new WallpaperSetter(getApplicationContext());
            wallpapersetter.refreshFromCurrent(holder);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            Log.d("GOI-WALLPAPER","onOffsetsChanged");
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
            Log.d("GOI-WALLPAPER", "onSurfaceRedrawNeeded");
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            /**
             * when wallpaper preview
             2022-02-13 20:54:49.254 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceCreated
             2022-02-13 20:54:49.254 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceChanged
             2022-02-13 20:54:49.255 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceRedrawNeeded
             2022-02-13 20:54:49.255 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             2022-02-13 20:54:49.255 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilityfalse
             2022-02-13 20:54:49.265 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             2022-02-13 20:54:49.279 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceRedrawNeeded
             2022-02-13 20:54:49.280 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onOffsetsChanged

             * when wallpaper set both
             2022-02-13 20:55:52.914 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: observe added from livewallpaper create
             2022-02-13 20:55:52.951 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceCreated
             2022-02-13 20:55:52.951 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceChanged
             2022-02-13 20:55:52.951 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceRedrawNeeded
             2022-02-13 20:55:52.951 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             2022-02-13 20:55:52.951 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilityfalse
             2022-02-13 20:55:52.969 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilityfalse
             2022-02-13 20:55:53.688 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onSurfaceDestroyed, isPreview=true

             * on home button
             2022-02-13 20:56:58.878 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onOffsetsChanged
             2022-02-13 20:56:58.878 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             2022-02-13 20:56:58.943 6610-6610/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onOffsetsChanged


             * when wallpaper becomes visible after a wallpaper change
             2022-02-12 15:11:04.688 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onOffsetsChanged
             2022-02-12 15:11:04.688 2662-2662/gnn.com.googlealbumdownloadappnougat D/GOI-WALLPAPER: onVisibilitytrue
             */
            super.onVisibilityChanged(visible);
            Log.d("GOI-WALLPAPER","onVisibility" + Boolean.toString(visible));
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.d("GOI-WALLPAPER","onSurfaceDestroyed, isPreview=" + isPreview());
        }

        @Override
        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight);
            Log.d("GOI-WALLPAPER","onDesiredSizeChanged");
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.d("GOI-WALLPAPER","onSurfaceChanged");
        }
    }

    private File getFolder(String path) {
        return Environment.getExternalStoragePublicDirectory(path);
    }
}