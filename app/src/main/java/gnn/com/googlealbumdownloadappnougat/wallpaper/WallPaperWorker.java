package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerDelayedAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.service.Cache;
import gnn.com.photos.sync.ChooseOneLocalPhotoPersist;
import gnn.com.photos.sync.SynchronizerDelayed;

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
        Logger.configure(getApplicationContext().getCacheDir().getAbsolutePath());
        Logger logger = Logger.getLogger();
        try {
                logger.info("start wallpaper worker, @WallPaperWork=" + this.hashCode());

                String destinationPath = getInputData().getString("folderPath");
                File destinationFolder = getDestinationFolder(destinationPath);

                int delaySyncMinute = getInputData().getInt("syncMaxAge", SynchronizerDelayed.DELAY_ALWAYS_SYNC);

                // TODO Need that frequencies was persisted before schedule
                PersistPrefMain persistMain = new PersistPrefMain(getApplicationContext());
                int frequencyUpdateHour = persistMain.getFrequencyUpdatePhotos();

                File cacheFile = new File(getInputData().getString("cacheAbsolutePath"));
                File processFolder = new File(getInputData().getString("processAbsolutePath"));

                logger.finest("WallpaperWorker parameters " + destinationPath + ", delaySync=" + delaySyncMinute + ", delayUpdate=" + frequencyUpdateHour);

                String albumName = getInputData().getString("album");
                String rename = getInputData().getString("rename");
                int quantity = getInputData().getInt("quantity", -1);

                try {
                    // make a sync to download photo if necessary
                    SynchronizerDelayedAndroid sync = new SynchronizerDelayedAndroid(delaySyncMinute, getApplicationContext(), cacheFile, frequencyUpdateHour, processFolder);
                    sync.syncRandom(albumName, destinationFolder, rename, quantity);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                    new Notification(getApplicationContext()).show(e.getMessage());
                }

                ChooseOneLocalPhotoPersist chooser = ChooseOneLocalPhotoPersist.getInstance(destinationFolder, processFolder);
                chooser.chooseOne();
                return Result.success();
            } catch (Exception e) {
                logger.severe(e.getMessage());
                new Notification(getApplicationContext()).show(e.getMessage());
                return Result.failure();
            } finally {
                logger.close();
            }
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
