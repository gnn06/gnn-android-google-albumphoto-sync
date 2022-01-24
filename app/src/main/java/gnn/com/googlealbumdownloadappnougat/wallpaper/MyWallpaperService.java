package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

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
            super.onCreate(surfaceHolder);
            Log.d("GOI","onCreate, isPreview=" + isPreview());
            if (!isPreview()) {
                PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(TestWork.class, 15, TimeUnit.MINUTES)
                        .build();
                WorkManager.getInstance()
                        .enqueueUniquePeriodicWork("GNN-TEST-WORK", ExistingPeriodicWorkPolicy.REPLACE, request);
                Log.d("GOI", "request enqueued");
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
}