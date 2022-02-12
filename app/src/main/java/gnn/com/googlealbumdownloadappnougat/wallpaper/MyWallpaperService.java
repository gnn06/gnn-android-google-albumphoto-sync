package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.photos.sync.ChooseOneLocalPhotoPersist;

/**
 * onCreate, onSurfaceCreated, onSurfaceChanged, onSurfaceREdraNeeded, onVisibility true, false, true, onOffsetsChanged
 */

public class MyWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {
        public MyWallpaperEngine() {}

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            /**
             * on reboot phone, it is called, previous wallpaper is not restored
             */
            super.onCreate(surfaceHolder);
            Log.d("GOI","onCreate, isPreview=" + isPreview());
            if (!isPreview()) {
                // TODO avoid have to give parameters
                File photoFolder = getFolder(new PersistPrefMain(getApplicationContext()).getPhotoPath());
                File processPath = getFolder(ApplicationContext.getInstance(getApplicationContext()).getProcessPath());
                ChooseOneLocalPhotoPersist chooser = ChooseOneLocalPhotoPersist.getInstance(photoFolder, processPath);
                WallpaperSetter wallpaperSetter = new WallpaperSetter(getApplicationContext());
                chooser.addObserver(wallpaperSetter);
                Log.d("GOI-WALLPAPER","observe added from livewallpaper create");
                // TODO set wallpaper on create.
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Log.d("GOI","onSurfaceCreated");
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            Log.d("GOI","onOffsetsChanged");
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
            Log.d("GOI", "onSurfaceRedrawNeeded");
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            Log.d("GOI","onVisibility" + Boolean.toString(visible));
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.d("GOI","onSurfaceDestroyed, isPreview=" + isPreview());
        }

        @Override
        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight);
            Log.d("GOI","onDesiredSizeChanged");
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.d("GOI","onSurfaceChanged");
        }
    }

    private File getFolder(String path) {
        return Environment.getExternalStoragePublicDirectory(path);
    }
}