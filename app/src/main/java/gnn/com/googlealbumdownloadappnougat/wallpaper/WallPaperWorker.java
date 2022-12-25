package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerDelayedAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.FrequencyCacheDelayConverter;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.sync.ChooseOneLocalPhotoPersist;

/**
 * choose a photo with ChooserSetterWAllpaper and make Synchronization last sync was expired
 * params :
 * - from ChooserSetter, folder where choose photo
 * - from SyncrhonizerDelayed, sync delay
 * - from Syncrhonizer.sync context, cacheAbsolutePath, cacheMaxAge, processAbsolutePath
 * - from Syncrhonizer.sync, albumName, rename, quantity
 */
public class WallPaperWorker extends Worker {

    public static final String PARAM_FOLDER_PATH = "folderPath";
    public static final String PARAM_CACHE_ABSOLUTE_PATH = "cacheAbsolutePath";
    public static final String PARAM_PROCESS_ABSOLUTE_PATH = "processAbsolutePath";
    public static final String PARAM_ALBUM = "album";
    public static final String PARAM_RENAME = "rename";
    public static final String PARAM_QUANTITY = "quantity";

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

                String destinationPath = getInputData().getString(PARAM_FOLDER_PATH);
                File destinationFolder = getDestinationFolder(destinationPath);

                // TODO Need that frequencies was persisted before schedule
                PersistPrefMain persistMain = new PersistPrefMain(getApplicationContext());

                int frequencySyncHour = persistMain.getFrequencyDownload();
                int frequencyUpdateHour = persistMain.getFrequencyUpdatePhotos();

                File cacheFile = new File(getInputData().getString(PARAM_CACHE_ABSOLUTE_PATH));
                File processFolder = new File(getInputData().getString(PARAM_PROCESS_ABSOLUTE_PATH));

                logger.finest("WallpaperWorker parameters " + destinationPath + ", delaySync=" + frequencySyncHour + ", delayUpdate=" + frequencyUpdateHour);

                int delaySyncMin = FrequencyCacheDelayConverter.getFrequencySyncHourMinute(frequencySyncHour);
                int delayUpdateHour = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosHourHour(frequencyUpdateHour);

                String albumName = getInputData().getString(PARAM_ALBUM);
                String rename = getInputData().getString(PARAM_RENAME);
                int quantity = getInputData().getInt(PARAM_QUANTITY, -1);

                try {
                    // make a sync to download photo if necessary
                    SynchronizerDelayedAndroid sync = new SynchronizerDelayedAndroid(delaySyncMin, getApplicationContext(), cacheFile, delayUpdateHour, processFolder);
                    sync.syncRandom(albumName, destinationFolder, rename, quantity);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                    new Notification(getApplicationContext()).show(e.getMessage());
                }

                ChooseOneLocalPhotoPersist chooser = ChooseOneLocalPhotoPersist.getInstance(destinationFolder, processFolder);
                chooser.chooseOne();
                Log.d("GNNAPP","wallpaper work finished");
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
