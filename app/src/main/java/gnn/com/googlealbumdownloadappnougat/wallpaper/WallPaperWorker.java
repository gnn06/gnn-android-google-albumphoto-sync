package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerDelayedAndroid;
import gnn.com.googlealbumdownloadappnougat.util.Logger;

/**
 * choose a photo with ChooserSetterWAllpaper and make Synchronization last sync was expired
 * params :
 * - from ChooserSetter, folder where choose photo
 * - from SyncrhonizerDelayed, sync delay
 * - from Syncrhonizer.sync context, cacheAbsolutePath, cacheMaxAge, processAbsolutePath
 * - from Syncrhonizer.sync, albumName, rename, quantity
 */
public class WallPaperWorker extends Worker {

    public WallPaperWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Logger logger = Logger.getLogger(getApplicationContext().getCacheDir().getAbsolutePath() + "/gnnapp.log");
            try {
                logger.info("start wallpaper worker");
                String destinationPath = getInputData().getString("folderPath");
                File destinationFolder = getDestinationFolder(destinationPath);

                int delay = getInputData().getInt("syncMaxAge", 0);

                File cacheFile = new File(getInputData().getString("cacheAbsolutePath"));
                long cacheMaxAge = getInputData().getLong("cacheMaxAge", -1);
                File processFolder = new File(getInputData().getString("processAbsolutePath"));

                logger.finest("WallpaperWorker parameters " + destinationPath + ", delay=" + delay);

                String albumName = getInputData().getString("album");
                String rename = getInputData().getString("rename");
                int quantity = getInputData().getInt("quantity", -1);

                // make a sync to download photo if necessary
                SynchronizerDelayedAndroid sync = new SynchronizerDelayedAndroid(delay, getApplicationContext(), cacheFile, cacheMaxAge, processFolder);
                sync.syncRandom(albumName, destinationFolder, rename, quantity);

                ChooserSetterWallPaper chooserSetterWallPaper =
                        new ChooserSetterWallPaper(getApplicationContext(), destinationFolder,
                                processFolder);
                chooserSetterWallPaper.setWallpaper();
                return Result.success();
            } catch (Exception e) {
                logger.severe(e.getMessage());
                return Result.failure();
            } finally {
                logger.close();
            }
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
