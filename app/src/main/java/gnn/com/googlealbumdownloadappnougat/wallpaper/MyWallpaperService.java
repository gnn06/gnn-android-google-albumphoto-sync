package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;

import gnn.com.photos.sync.ChooseOneLocalPhotoPersist;
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
            super.onCreate(surfaceHolder);
            Log.d("GOI","onCreate, isPreview=" + isPreview());
            if (!isPreview()) {
                ChooseOneLocalPhotoPersist chooser = ChooseOneLocalPhotoPersist.getInstance();
                chooser.addObserver(this);
                Log.d("GOI-WALLPAPER","observe added from livewallpaper create");
            }
        }

        @Override
        public void onDestroy() {
            // called when live wallpaper is dessactivate (isPreview == false)
            super.onDestroy();
            Log.d("GOI-WALLPAPER","onDestroy");
            if (!isPreview()) {
                ChooseOneLocalPhotoPersist chooser = ChooseOneLocalPhotoPersist.getInstance(null, null);
                chooser.removeObserver(this);
            }
        }

        @Override
        public void onWallpaper() {
            WallpaperSetter wallpaperSetter = new WallpaperSetter(getApplicationContext());
            wallpaperSetter.refreshFromCurrent(getSurfaceHolder());
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Log.d("GOI-WALLPAPER","onSurfaceCreated");
        }

//        @Override
//        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
//            // called quand on fait glisser le desktop pour voir les icones des apps de gauche et droite
//            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
//            Log.d("GOI-WALLPAPER","onOffsetsChanged");
//        }

//        @Override
//        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
//            super.onSurfaceRedrawNeeded(holder);
//            Log.d("GOI-WALLPAPER", "onSurfaceRedrawNeeded");
//        }

//        @Override
//        public void onVisibilityChanged(boolean visible) {
//            super.onVisibilityChanged(visible);
//            Log.d("GOI-WALLPAPER","onVisibility" + visible);
//        }

//        @Override
//        public void onSurfaceDestroyed(SurfaceHolder holder) {
//            super.onSurfaceDestroyed(holder);
//            Log.d("GOI-WALLPAPER","onSurfaceDestroyed, isPreview=" + isPreview());
//        }

//        @Override
//        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
//            super.onDesiredSizeChanged(desiredWidth, desiredHeight);
//            Log.d("GOI-WALLPAPER","onDesiredSizeChanged");
//        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.d("GOI-WALLPAPER","onSurfaceChanged" + format + "," + width + "x" + height);
            WallpaperSetter wallpapersetter = new WallpaperSetter(getApplicationContext());
            wallpapersetter.refreshFromCurrent(holder);
        }
    }

    private File getFolder(String path) {
        return Environment.getExternalStoragePublicDirectory(path);
    }

    public boolean isActive(Context context) {
        WallpaperManager wlppMgr = WallpaperManager.getInstance(context);
        WallpaperInfo wlppInfo = wlppMgr != null ? wlppMgr.getWallpaperInfo() : null;
        Log.d("GOI-WALLPAPER", "wallpaperinfo.packagename=" +
                (wlppInfo != null ? wlppInfo.getPackageName() : "no package"));
        return wlppInfo != null && wlppInfo.getPackageName().equals("gnn.com.googlealbumdownloadappnougat");
    }
}