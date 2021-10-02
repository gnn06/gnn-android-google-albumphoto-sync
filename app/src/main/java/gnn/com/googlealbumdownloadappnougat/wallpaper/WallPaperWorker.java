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
 * - from SyncrhonizerDelayed, delay
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
            String destinationPath = getInputData().getString("folderPath");
            File destinationFolder = getDestinationFolder(destinationPath);

            long delay = getInputData().getLong("syncMaxAge", 0);

            File cacheFile = new File(getInputData().getString("cacheAbsolutePath"));
            long cacheMaxAge = getInputData().getLong("cacheMaxAge", -1);
            File processFolder = new File(getInputData().getString("processAbsolutePath"));

            logger.info("Wallpaper.doWork start @logger=" + logger.hashCode() + ", @logger=" + logger.hashCode() + ", @fileHandler=" + logger.getHandlers()[0].hashCode());
            logger.info("WallpaperWorker parameters " + destinationPath);

            String albumName = getInputData().getString("album");
            String rename = getInputData().getString("rename");
            int quantity = getInputData().getInt("quantity", -1);

            // make a sync to doownload photo if necessary
            SynchronizerDelayedAndroid sync = new SynchronizerDelayedAndroid(delay, getApplicationContext(), cacheFile, cacheMaxAge, processFolder);
            sync.syncRandom(albumName, destinationFolder, rename, quantity);

            ChooserSetterWallPaper chooserSetterWallPaper = new ChooserSetterWallPaper(getApplicationContext(), destinationFolder);
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
