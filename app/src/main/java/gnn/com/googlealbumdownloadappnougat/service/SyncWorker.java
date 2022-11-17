package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerWorker;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.FrequencyCacheDelayConverter;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallPaperWorker;
import gnn.com.photos.service.Cache;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.sync.Synchronizer;

public class SyncWorker extends Worker {

    private final static String TAG = "MyWorker";

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        PersistPrefMain persistPrefMain = new PersistPrefMain(getApplicationContext());

        File cacheFile = new File(getInputData().getString(WallPaperWorker.PARAM_CACHE_ABSOLUTE_PATH));
        int frequencyUpdateHour = persistPrefMain.getFrequencyUpdatePhotos();
        File processFolder = new File(getInputData().getString(WallPaperWorker.PARAM_PROCESS_ABSOLUTE_PATH));

        int delayUpdateHour = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosHourHour(frequencyUpdateHour);

        Context activity = getApplicationContext();
        Synchronizer synchronizer = new SynchronizerWorker(activity, cacheFile, delayUpdateHour, processFolder, this);

        String albumName = getInputData().getString(WallPaperWorker.PARAM_ALBUM);
        String destinationFolder = getInputData().getString(WallPaperWorker.PARAM_FOLDER_PATH);
        String rename = getInputData().getString(WallPaperWorker.PARAM_RENAME);
        int quantity = getInputData().getInt(WallPaperWorker.PARAM_QUANTITY, -1);

        // Doc Periodic work is never successed, always enqueued

        try {
            synchronizer.syncRandom(albumName, getDestinationFolder(destinationFolder), rename, quantity);
            Log.i(TAG, "success");
            // Doc periodic outputData is always empty
            Log.d("GNNAPP","SyncWork finished");
            // With a Periodic work, output is always null as (said in android doc) output is only available in FINISHED state
            return Result.success();
        } catch (IOException | RemoteException e) {
            Log.e(TAG, e.toString());
            return Result.failure();
        }
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
