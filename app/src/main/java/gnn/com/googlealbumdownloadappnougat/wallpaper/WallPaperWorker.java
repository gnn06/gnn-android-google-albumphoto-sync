package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.util.logging.Logger;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerDelayedAndroid;
import gnn.com.googlealbumdownloadappnougat.util.AppLogger;

/**
 * choose a photo with ChooserSetterWAllpaper and make Synchronization last sync was expired
 * params :
 * - from ChooserSetter, folder where choose photo
 * - from SyncrhonizerDelayed, sync delay
 * - from Syncrhonizer.sync context, cacheAbsolutePath, cacheMaxAge, processAbsolutePath
 * - from Syncrhonizer.sync, albumName, rename, quantity
 */
public class WallPaperWorker extends Worker {

    private final static String TAG = "WalllPaperWorker";

    public WallPaperWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Logger logger = AppLogger.getLogger(getApplicationContext());
        try {
            logger.info("start wallpaper worker");
            String destinationPath = getInputData().getString("folderPath");
            File destinationFolder = getDestinationFolder(destinationPath);

            int delay = getInputData().getInt("syncMaxAge", 0);

            File cacheFile = new File(getInputData().getString("cacheAbsolutePath"));
            long cacheMaxAge = getInputData().getLong("cacheMaxAge", -1);
            File processFolder = new File(getInputData().getString("processAbsolutePath"));

            logger.finest("Wallpaper.doWork start thread id=" + Thread.currentThread().getId() + ", @logger=" + logger.hashCode() + ", @logger=" + logger.hashCode() + ", @fileHandler=" + logger.getHandlers()[0].hashCode());
            logger.finest("WallpaperWorker parameters " + destinationPath + ", delay=" + delay);

            String albumName = getInputData().getString("album");
            String rename = getInputData().getString("rename");
            int quantity = getInputData().getInt("quantity", -1);

            // make a sync to doownload photo if necessary
            SynchronizerDelayedAndroid sync = new SynchronizerDelayedAndroid(delay, getApplicationContext(), cacheFile, cacheMaxAge, processFolder);
            sync.syncRandom(albumName, destinationFolder, rename, quantity);

            ChooserSetterWallPaper chooserSetterWallPaper =
                    new ChooserSetterWallPaper(getApplicationContext(), destinationFolder,
                            processFolder);
            chooserSetterWallPaper.setWallpaper();
            logger.getHandlers()[0].close();
            return Result.success();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            logger.getHandlers()[0].close();
            return Result.failure();
        }
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
