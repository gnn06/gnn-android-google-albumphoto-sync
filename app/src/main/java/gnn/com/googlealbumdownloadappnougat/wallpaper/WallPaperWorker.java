package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.PowerManager;

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


    private BroadcastReceiver receiver;

    private void method() {
        if (receiver == null) {
            receiver = new MyBroadcastReceiver();
            IntentFilter intent = new IntentFilter(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
            getApplicationContext().registerReceiver(receiver, intent);
            Logger logger = Logger.getLogger();
            logger.fine("registered receiver after work");
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        Logger.configure(getApplicationContext().getCacheDir().getAbsolutePath());
        Logger logger = Logger.getLogger();
        try {
                logger.info("start wallpaper worker, @WallPaperWork=" + this.hashCode());

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

                try {
                    // make a sync to download photo if necessary
                    SynchronizerDelayedAndroid sync = new SynchronizerDelayedAndroid(delay, getApplicationContext(), cacheFile, cacheMaxAge, processFolder);
                    sync.syncRandom(albumName, destinationFolder, rename, quantity);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                    new Notification(getApplicationContext()).show(e.getMessage());
                }

                ChooserSetterWallPaper chooserSetterWallPaper =
                        new ChooserSetterWallPaper(getApplicationContext(), destinationFolder,
                                processFolder);
                chooserSetterWallPaper.setWallpaper();
                method();
                return Result.success();
            } catch (Exception e) {
                logger.severe(e.getMessage());
                new Notification(getApplicationContext()).show(e.getMessage());
                method();
                return Result.failure();
            } finally {
                logger.close();
            }
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
